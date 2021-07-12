package it.unisannio.vehicle.service;

import it.unisannio.vehicle.Utils;
import it.unisannio.vehicle.dto.NextStationDTO;
import it.unisannio.vehicle.dto.NextStationRequestDTO;
import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.*;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.pojo.Ride;
import it.unisannio.vehicle.pojo.VehicleStatus;
import it.unisannio.vehicle.repository.VehicleRepository;
import it.unisannio.vehicle.websocket.WebSocketClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MovingService {

    private final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private TripService tripService;
    private VehicleRepository vehicleRepository;
    private WebSocketService webSocketService;
    private TrafficService trafficService;
    private ArtemisService artemisService;

    @Autowired
    public MovingService(TripService tripService, VehicleRepository vehicleRepository, WebSocketService webSocketService, TrafficService trafficService) {
        this.tripService = tripService;
        this.vehicleRepository = vehicleRepository;
        this.webSocketService = webSocketService;
        this.trafficService = trafficService;
    }

    public void displacement() {
        this.displacement(null);
    }

    public void displacement(List<String> licensePlates) {
        List<Vehicle> vehicleList;
        if (licensePlates != null) {
            vehicleList = this.vehicleRepository.getAllStoppedVehiclesWithLicensePlate(licensePlates);
        } else {
            vehicleList = this.vehicleRepository.getAllStoppedVehicles();
        }

        StatisticsDTO statistics = this.tripService.getTripStatistics();
        List<RouteStatsDTO> routeStatsList = statistics.getRouteStatsList();
        int numberOfAvailableVehicles = vehicleList.size();
        int vehicleElem = 0;
        Ride ride;
        if (routeStatsList != null && numberOfAvailableVehicles > 0) {

            // assign one vehicle for each route
            for (RouteStatsDTO routeStats : routeStatsList) {
                if (numberOfAvailableVehicles == 0) break;

                ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                ride.setCurrentStation(
                        new Station(routeStats.getStations().get(0).getNodeId(), routeStats.getStations().get(0).getPosition()));
                vehicleList.get(vehicleElem).setRide(ride);
                this.vehicleRepository.save(vehicleList.get(vehicleElem));
                vehicleElem++;
                numberOfAvailableVehicles--;
            }

            final int totalVehicles = numberOfAvailableVehicles;

            for (RouteStatsDTO routeStats : routeStatsList) {
                if (numberOfAvailableVehicles == 0) break;

                if (routeStats.getRequests() == 0) {
                    ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                    ride.setCurrentStation(ride.getRoute().get(0));
                    vehicleList.get(vehicleElem).setRide(ride);
                    this.vehicleRepository.save(vehicleList.get(vehicleElem));
                    vehicleElem++;
                    numberOfAvailableVehicles--;
                } else {
                    int p = (int) Math.round((double)routeStats.getRequests() / (double)statistics.getAllTripRequests() * 100);
                    // vehiclesToAssignAtRoute: number of vehicles with the same route
                    final int vehiclesToAssignAtRoute = (int) Math.round((double) p * totalVehicles / 100);
                    ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));

                    int vehiclesAlreadyAssigned = 0;
                    for (StationStatsDTO stationStats : routeStats.getStations()) {

                        // no more available vehicles to assign
                        if (vehiclesToAssignAtRoute - vehiclesAlreadyAssigned == 0) break;

                        p = (int) Math.round((double) stationStats.getRequests() / (double) routeStats.getRequests() * 100);
                        final int vehiclesStation = (int) Math.round((double) p * vehiclesToAssignAtRoute / 100);
                        ride.setCurrentStation(new Station(stationStats.getNodeId(), stationStats.getPosition()));

                        for (int i = vehicleElem; i < vehicleElem + vehiclesStation; i++) {
                            vehicleList.get(i).setRide(ride);
                            this.vehicleRepository.save(vehicleList.get(i));
                            vehiclesAlreadyAssigned++;
                        }
                        vehicleElem += vehiclesStation;
                        numberOfAvailableVehicles -= vehiclesStation;
                    }
                }
            }

            // send displacement station to topic (each vehicle-ms instance get only Displacement
            for (Vehicle v : vehicleList) {
                VehicleDisplacementDTO vehicleDisplacement = new VehicleDisplacementDTO(v.getLicensePlate(), v.getRide().getCurrentStation());
                this.artemisService.send(artemisService.getVehicleDisplacementTopic(), vehicleDisplacement);
            }
        }
    }

    public void sendDisplacementNotification(VehicleDisplacementDTO vehicleDisplacement) {
        try {
            NextStationDTO nextStationDTO = new NextStationDTO(vehicleDisplacement.getNextStation());
            this.webSocketService.sendMessage(vehicleDisplacement.getLicensePlate(), nextStationDTO);
        } catch (WebSocketClientNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    public VehicleStatus requestAssignment(TripDTO trip) {
        VehicleStatus vehicleStatus = null;

        List<Vehicle> vehicleList =
                this.vehicleRepository.findByNodeIdsInRouteAndAvailableSeatsAndOrderByOccupiedSeatsDesc(List.of(trip.getSource()));

        boolean vehicleFound = false;
        if (vehicleList.size() > 0) {
            for (Vehicle vehicle : vehicleList) {
                if (srcAndDestInPickPoints(vehicle.getRide().getPickPoints(), trip.getSource(), trip.getDestination())) {
                    this.updatePickPointForVehicle(vehicle, trip);
                    vehicleStatus = this.prepareVehicleStatusObject(vehicle);
                    vehicleFound = true;
                    break;
                }
            }

            // It was not found vehicle optimal vehicle... assign request to first vehicle available...
            if (!vehicleFound) {
                this.updatePickPointForVehicle(vehicleList.get(0), trip);
                vehicleStatus = this.prepareVehicleStatusObject(vehicleList.get(0));
            }
        } else {
            vehicleStatus = new VehicleStatus(VehicleStatus.Status.FULL);
        }
        return vehicleStatus;
    }

    public void startRideForVehicleAndSendTripNotification(Vehicle vehicle) {
        // we can send a TripNotification for each request (PickPoints of vehicle)
        for (PickPoint pickPoint : vehicle.getRide().getPickPoints()) {
            this.sendTripNotification(
                    pickPoint.getTripId(),
                    TripNotificationDTO.Status.APPROVED,
                    vehicle.getLicensePlate(),
                    pickPoint.getSourceNodeId());
        }

        vehicle.getRide().setMoving(true);
        NextStationDTO nextStationDTO = findNextStation(vehicle.getRide().getPickPoints(), vehicle.getRide().getRoute(), vehicle.getRide().getCurrentStation());
        try {
            this.webSocketService.sendMessage(vehicle.getLicensePlate(), nextStationDTO);
            this.vehicleRepository.save(vehicle);
        } catch (WebSocketClientNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    public void sendTripNotification(String tripId, TripNotificationDTO.Status status, String licensePlate, Integer sourceId) {
        TripNotificationDTO tripNotification = new TripNotificationDTO();
        tripNotification.setTripId(tripId);
        tripNotification.setVehicleLicensePlate(licensePlate);
        tripNotification.setPickUpNodeId(sourceId);
        tripNotification.setStatus(status != null ? status : TripNotificationDTO.Status.APPROVED);
        this.artemisService.send(artemisService.getTripConfirmationTopic(), tripNotification);
    }

    public NextStationDTO getNextStation(String sessionId, NextStationRequestDTO request) {
        NextStationDTO nextStation = null;
        String licensePlate = this.webSocketService.getLicensePlateFromWebsocket(sessionId);
        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findByLicensePlate(licensePlate);

        if (vehicleOptional.isPresent()
                && vehicleOptional.get().getRide() != null
                && vehicleOptional.get().getRide().getPickPoints() != null
                && request != null) {

            Vehicle vehicle = vehicleOptional.get();
            List<PickPoint> pickPoints = vehicle.getRide().getPickPoints();

            // Updating PickPoint for vehicle

            Station currentStation = request.getCurrentStation();
            Intersection intersection = this.trafficService.getIntersections(currentStation.getNodeId());
            currentStation.setPosition(intersection.getCoordinate());

            Iterator<PickPoint> iterator = pickPoints.iterator();
            vehicle.getRide().setCurrentStation(currentStation);
            int numPickPoints = pickPoints.size();
            while (iterator.hasNext()) {
                PickPoint pp = iterator.next();
                if (pp.getDestinationNodeId().equals(currentStation.getNodeId()) && pp.getStatus().equals(PickPoint.Status.ONBOARDED)) {
                    vehicle.decrementOccupiedSeat();
                    iterator.remove(); // release passenger...
                } else if (pp.getSourceNodeId().equals(currentStation.getNodeId()) && pp.getStatus().equals(PickPoint.Status.WAIT)) {
                    pp.setStatus(PickPoint.Status.ONBOARDED);
                }
            }

            // reset initialWaitingDate if vehicle return to 0 passengers
            if (numPickPoints > 0 && vehicle.getRide().getPickPoints().size() == 0)
                vehicle.getRide().setInitialWaitingDate(new Date());

            vehicle.getRide().setPickPoints(pickPoints);
            this.vehicleRepository.save(vehicle);


            nextStation = this.findNextStation(pickPoints, vehicle.getRide().getRoute(), vehicle.getRide().getCurrentStation());
        } else if (vehicleOptional.isPresent()
                && vehicleOptional.get().getRide() != null) {
            nextStation = new NextStationDTO(vehicleOptional.get().getRide().getCurrentStation());
        }

        return nextStation;
    }

    public void checkVehicleTemporalParameters() {
        List<String> vehiclesToDisplacement = new ArrayList<>();
        List<Vehicle> vehicleList = this.vehicleRepository.findAll();
        Date now = Utils.convertDateToUTC(new Date());
        for (Vehicle vehicle : vehicleList) {
            if (!vehicle.getRide().isMoving()) {
                int waitingTime = (int) TimeUnit.MILLISECONDS.toMinutes(now.getTime() - vehicle.getRide().getInitialWaitingDate().getTime());
                if (waitingTime > vehicle.getInertialTimeTarget()
                        && vehicle.getOccupiedSeats() > 0) {
                    this.startRideForVehicleAndSendTripNotification(vehicle);
                } else if (waitingTime > vehicle.getWaitingTimeTarget()
                        && vehicle.getOccupiedSeats() == 0) {
                    vehiclesToDisplacement.add(vehicle.getLicensePlate());
                }
            }
        }
        if (vehiclesToDisplacement.size() > 0) {
            this.displacement(vehiclesToDisplacement);
        }
    }

    private NextStationDTO findNextStation(List<PickPoint> pickPoints, List<Station> route, Station currentStation) {
        NextStationDTO nextStation = null;
        if (pickPoints.size() > 0) {
            int i = route.indexOf(currentStation);
            int iterations = 0;
            while (iterations < route.size()) {
                if (isANextStation(route.get(i), pickPoints)) {
                    List<Coordinate> minPath = this.trafficService.shortestPath(currentStation.getNodeId(), route.get(i).getNodeId());
                    nextStation = new NextStationDTO(route.get(i), minPath);
                    break;
                }
                i = i + 1 % route.size();
                iterations++;
            }
        }
        return nextStation;
    }

    private boolean isANextStation(Station station, List<PickPoint> pickPoints) {
        boolean isANextStation = false;
        for (PickPoint pickPoint : pickPoints) {
            isANextStation = isANextStation || (
                    (pickPoint.getSourceNodeId().equals(station.getNodeId()) && pickPoint.getStatus().equals(PickPoint.Status.WAIT))
                            ||
                            (pickPoint.getDestinationNodeId().equals(station.getNodeId()) && pickPoint.getStatus().equals(PickPoint.Status.ONBOARDED))
            );
        }

        return isANextStation;
    }

    private void updatePickPointForVehicle(Vehicle vehicle, TripDTO trip) {
        vehicle.addPickPoint(new PickPoint(trip.getId(), trip.getSource(), trip.getDestination()));
        this.vehicleRepository.save(vehicle);
    }

    private VehicleStatus prepareVehicleStatusObject(Vehicle vehicle) {
        VehicleStatus vehicleStatus;
        if (!vehicle.getRide().isMoving() && vehicle.isOccupancyTargetReached()) {
            vehicleStatus = new VehicleStatus(vehicle, VehicleStatus.Status.READY_TO_START);
        } else if (vehicle.getRide().isMoving()) {
            vehicleStatus = new VehicleStatus(vehicle, VehicleStatus.Status.MOVING);
        } else
            vehicleStatus = new VehicleStatus(VehicleStatus.Status.WAIT_PASSENGERS);
        return vehicleStatus;
    }

    private boolean srcAndDestInPickPoints(List<PickPoint> pickPoints, Integer srcNodeId, Integer destNodeId) {
        boolean srcFound = false, destFound = false;
        for (PickPoint pickPoint : pickPoints) {
            srcFound = srcFound || pickPoint.getSourceNodeId().equals(srcNodeId) || pickPoint.getDestinationNodeId().equals(srcNodeId);
            destFound = destFound || pickPoint.getSourceNodeId().equals(destNodeId) || pickPoint.getDestinationNodeId().equals(destNodeId);
        }
        return srcFound && destFound;
    }
}
