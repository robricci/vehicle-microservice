package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.*;
import it.unisannio.vehicle.dto.internal.RouteDTO;
import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VehicleService {

    private final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private VehicleRepository vehicleRepository;
    private MovingService movingService;
    private TripService tripService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, MovingService movingService, TripService tripService) {
        this.vehicleRepository = vehicleRepository;
        this.movingService = movingService;
        this.tripService = tripService;
    }

    public List<VehicleDTO> getVehiclesInfo() {
        List<Vehicle> vehicles = this.vehicleRepository.findAll();
        return VehicleDTO.convert(vehicles);
    }

    public VehicleDTO getVehicleInfo(String licensePlate) {
        VehicleDTO vehicleDTO = null;
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle.isPresent()) {
            vehicleDTO = new VehicleDTO(vehicle.get());
        }
        return vehicleDTO;
    }

    public void settingParams(String licensePlate, VehicleParamDTO vehicleParam) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle.isPresent()) {
            vehicle.get().setOccupancyTarget(vehicleParam.getOccupancyTarget());
            vehicle.get().setWaitingTimeTarget(vehicleParam.getWaitingTimeTarget());
            vehicle.get().setInertialTimeTarget(vehicleParam.getInertialTimeTarget());
            this.vehicleRepository.save(vehicle.get());
        }
    }

    @Transactional
    public void insertVehicles(List<InsertVehicleDTO> vehicleList) {
        List<String> licensePlates = new ArrayList<>();
        for (InsertVehicleDTO newVehicle : vehicleList) {
            Vehicle v = new Vehicle(
                    newVehicle.getLicensePlate(),
                    newVehicle.getTotalAvailableSeats(),
                    newVehicle.getWaitingTimeTarget(),
                    newVehicle.getOccupancyTarget(),
                    newVehicle.getInertialTimeTarget());
            this.vehicleRepository.insert(v);
            licensePlates.add(newVehicle.getLicensePlate());
        }
        this.movingService.displacement(licensePlates);
    }

    public boolean removeVehicle(String licensePlate) {
        Optional<Vehicle> removedVehicle = this.vehicleRepository.findByLicensePlateAndOccupiedSeatsAndRemove(licensePlate, 0);
        return removedVehicle.isPresent();
    }

    public boolean manualDisplacement(String licensePlate, ManualDisplacementDTO manualDisplacement) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle.isPresent()
                && !vehicle.get().getRide().isMoving()
                && vehicle.get().getOccupiedSeats() == 0
                && (vehicle.get().getRide().getPickPoints() == null
                || vehicle.get().getRide().getPickPoints().size() == 0) ) {
            List<RouteDTO> routes = this.tripService.getRoutes();
            Station currentStation = vehicle.get().getRide().getCurrentStation();
            for (RouteDTO route : routes) {
                if (route.getId().equals(manualDisplacement.getRouteId())) {
                    for (Station sta : route.getStations()) {
                        if (sta.equals(manualDisplacement.getStation())) {
                            vehicle.get().getRide().setRoute(route.getStations());
                            vehicle.get().getRide().setRouteId(route.getId());
                            vehicle.get().getRide().setCurrentStation(sta);
                            vehicle.get().getRide().setInitialWaitingDate(new Date());
                            this.vehicleRepository.save(vehicle.get());
                            this.movingService.manualDisplacementNotification(licensePlate, currentStation, vehicle.get().getRide().getCurrentStation());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
