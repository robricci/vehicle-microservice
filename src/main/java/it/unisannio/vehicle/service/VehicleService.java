package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.*;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VehicleService {

    private final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private VehicleRepository vehicleRepository;
    private MovingService movingService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, MovingService movingService) {
        this.vehicleRepository = vehicleRepository;
        this.movingService = movingService;
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
            vehicle.get().setInertialTimeTarget(vehicleParam.getInertialTime());
            this.vehicleRepository.save(vehicle.get());
        }
    }

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
}
