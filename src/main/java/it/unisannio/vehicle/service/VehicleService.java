package it.unisannio.vehicle.service;

import it.unisannio.vehicle.Utils;
import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.VehicleDTO;
import it.unisannio.vehicle.dto.internal.RouteStatsDTO;
import it.unisannio.vehicle.dto.internal.StatisticsDTO;
import it.unisannio.vehicle.model.Vehicle;
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
}
