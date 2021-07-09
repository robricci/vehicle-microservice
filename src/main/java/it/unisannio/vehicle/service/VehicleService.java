package it.unisannio.vehicle.service;

import it.unisannio.vehicle.Utils;
import it.unisannio.vehicle.dto.*;
import it.unisannio.vehicle.dto.internal.*;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.pojo.VehicleStatus;
import it.unisannio.vehicle.pojo.Ride;
import it.unisannio.vehicle.repository.VehicleRepository;
import it.unisannio.vehicle.websocket.WebSocketClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private VehicleRepository vehicleRepository;
    private TripService tripService;
    private WebSocketService webSocketService;
    private TrafficService trafficService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, TrafficService trafficService,
                          TripService tripService, WebSocketService webSocketService) {
        this.vehicleRepository = vehicleRepository;
        this.trafficService = trafficService;
        this.tripService = tripService;
        this.webSocketService = webSocketService;
    }

    public List<VehicleDTO> getVehiclesInfo() {
        List<Vehicle> vehicles = this.vehicleRepository.findAll();
        return VehicleDTO.convert(vehicles);
    }

    public VehicleDTO getVehicleInfo(String id) {
        VehicleDTO vehicleDTO = null;
        Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            vehicleDTO = new VehicleDTO(vehicle.get());
        }
        return vehicleDTO;
    }

    public void insertVehicles(List<InsertVehicleDTO> vehicleList) {
        for (InsertVehicleDTO newVehicle : vehicleList) {
            Vehicle v = new Vehicle(
                    newVehicle.getLicensePlate(),
                    newVehicle.getTotalAvailableSeats(),
                    newVehicle.getWaitingTimeTarget(),
                    newVehicle.getOccupancyTarget(),
                    newVehicle.getInertialTimeTarget());
            this.vehicleRepository.insert(v);
        }
    }

    public void displacement() {
        StatisticsDTO statistics = this.tripService.getTripStatistics();
        List<RouteStatsDTO> routeStatsList = statistics.getRouteStatsList();
        List<Vehicle> vehicleList = this.vehicleRepository.getAllStoppedVehicles();
        int numberOfAvailableVehicles = vehicleList.size();
        int vehicleElem = 0;
        Ride ride;
        if (routeStatsList != null && numberOfAvailableVehicles > 0) {

            // assign one vehicle for each route
            for (RouteStatsDTO routeStats : routeStatsList) {
                if (numberOfAvailableVehicles == 0) break;

                ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                ride.setAssignedStation(
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
                    ride.setAssignedStation(ride.getRoute().get(0));
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
                        ride.setAssignedStation(new Station(stationStats.getNodeId(), stationStats.getPosition()));

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

    public NextStationDTO getNextStation(String sessionId, NextStationRequestDTO request) {
        NextStationDTO nextStation = null;
        String licensePlate = this.webSocketService.getLicensePlateFromWebsocket(sessionId);
        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findByLicensePlate(licensePlate);

        if (vehicleOptional.isPresent() && vehicleOptional.get().getRide() != null
                && vehicleOptional.get().getRide().getPickPoints() != null) {

            Vehicle vehicle = vehicleOptional.get();
            List<PickPoint> pickPoints = vehicle.getRide().getPickPoints();

            // Updating PickPoint for vehicle
            Station currentStation = request.getCurrentStation();
            Iterator<PickPoint> iterator = pickPoints.iterator();
            vehicle.setLastKnownStation(currentStation);
            while (iterator.hasNext()) {
                PickPoint pp = iterator.next();
                if (pp.getDestinationNodeId().equals(currentStation.getNodeId()) && pp.getStatus().equals(PickPoint.Status.ONBOARDED)) {
                    vehicle.decrementOccupiedSeat();
                    iterator.remove(); // release passenger...
                } else if (pp.getSourceNodeId().equals(currentStation.getNodeId()) && pp.getStatus().equals(PickPoint.Status.WAIT)) {
                    pp.setStatus(PickPoint.Status.ONBOARDED);
                }
            }
            vehicle.getRide().setPickPoints(pickPoints);
            this.vehicleRepository.save(vehicle);


            nextStation = this.findNextStation(pickPoints, vehicle.getRide().getRoute(), vehicle.getLastKnownStation());
        }

        return nextStation;
    }

    public void startRideForVehicle(Vehicle vehicle) {
        vehicle.getRide().setMoving(true);

        NextStationDTO nextStationDTO = findNextStation(vehicle.getRide().getPickPoints(), vehicle.getRide().getRoute(), vehicle.getLastKnownStation());
        try {
            this.webSocketService.sendMessage(vehicle.getLicensePlate(), nextStationDTO);
        } catch (WebSocketClientNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    private NextStationDTO findNextStation(List<PickPoint> pickPoints, List<Station> route, Station lastKnownStation) {
        NextStationDTO nextStation = null;
        if (pickPoints.size() > 0) {
            int i = route.indexOf(lastKnownStation);
            int iterations = 0;
            while (iterations < route.size()) {
                if (isANextStation(route.get(i), pickPoints)) {
                    List<Coordinate> minPath = this.trafficService.shortestPath(lastKnownStation.getNodeId(), route.get(i).getNodeId());
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
