package it.unisannio.vehicle.service;

import it.unisannio.vehicle.Utils;
import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.VehicleDTO;
import it.unisannio.vehicle.dto.internal.RouteStatsDTO;
import it.unisannio.vehicle.dto.internal.StatisticsDTO;
import it.unisannio.vehicle.dto.internal.TripDTO;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.pojo.VehicleStatus;
import it.unisannio.vehicle.pojo.Ride;
import it.unisannio.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private TripService tripService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, TripService tripService) {
        this.vehicleRepository = vehicleRepository;
        this.tripService = tripService;
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
                    newVehicle.getLicenseId(),
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
        int totalVehicles = vehicleList.size();
        int vehicleElem = 0;
        Ride ride;
        if (routeStatsList != null && totalVehicles > 0) {
            if (totalVehicles <= routeStatsList.size()) {
                for (RouteStatsDTO routeStats : routeStatsList) {
                    ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                    vehicleList.get(vehicleElem).setRide(ride);
                    this.vehicleRepository.save(vehicleList.get(vehicleElem));
                    vehicleElem++;
                    if (--totalVehicles == 0) break;
                }
            } else { // # vehicles > # routes

                // Allocation of 1 vehicle for each route with zero requests
                // and other vehicles in routes with statistics based criteria
                for (RouteStatsDTO routeStats : routeStatsList) {
                    if (routeStats.getRequests() == 0) {
                        ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                        vehicleList.get(vehicleElem).setRide(ride);
                        this.vehicleRepository.save(vehicleList.get(vehicleElem));
                        vehicleElem++;
                        totalVehicles--;
                    } else {
                        int p = routeStats.getRequests() / statistics.getAllTripRequests() * 100;
                        int vehiclesToAssign = p * totalVehicles / 100;
                        for (int i = vehicleElem; i < vehicleElem + vehiclesToAssign; i++) {
                            ride = new Ride(Utils.convertStationStatsDtoListToStationList(routeStats.getStations()));
                            vehicleList.get(vehicleElem).setRide(ride);
                            this.vehicleRepository.save(vehicleList.get(vehicleElem));
                        }
                        vehicleElem += vehiclesToAssign;
                        totalVehicles -= vehiclesToAssign;
                    }
                }
            }
        }
    }

    public VehicleStatus requestAssignment(TripDTO trip, String tripJMSCorrelationId) {
        VehicleStatus vehicleStatus = null;

        // Veicoli che appartengono alla rotta del trip, con posti disponibili e ordinati per posti occupati in modo decrescente
        List<Vehicle> vehicleList =
                this.vehicleRepository.findRouteByNodeIdsAndAvailableSeatsAndOrderByOccupiedSeatsDesc(List.of(trip.getSource()));

        boolean vehicleFound = false;
        if (vehicleList.size() > 0) {
            for (Vehicle vehicle : vehicleList) {
                if (srcAndDestInPickPoints(vehicle.getRide().getPickPoints(), trip.getSource(), trip.getDestination())) {
                    this.updatePickPointForVehicle(vehicle, trip, tripJMSCorrelationId);
                    vehicleStatus = this.prepareVehicleStatusObject(vehicle);
                    vehicleFound = true;
                    break;
                }
            }
            // non è stato trovato un veicolo ottimo, assegnamo il primo con posti occupati maggiore (che avrà posti diponibili per certo)
            if (!vehicleFound) {
                this.updatePickPointForVehicle(vehicleList.get(0), trip, tripJMSCorrelationId);
                vehicleStatus = this.prepareVehicleStatusObject(vehicleList.get(0));
            }
        } else {
            vehicleStatus = new VehicleStatus(VehicleStatus.Status.FULL);
        }
        return vehicleStatus;
    }

    private void updatePickPointForVehicle(Vehicle vehicle, TripDTO trip, String tripJMSCorrelationId) {
        vehicle.addPickPoint(new PickPoint(trip.getId(), trip.getSource(), trip.getDestination(), tripJMSCorrelationId));
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
