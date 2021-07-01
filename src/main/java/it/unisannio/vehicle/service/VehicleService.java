package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.VehicleDTO;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.model.VehicleRide;
import it.unisannio.vehicle.repository.VehicleRepository;
import it.unisannio.vehicle.repository.VehicleRideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleRideRepository vehicleRideRepository;

    @Autowired
    public List<VehicleDTO> getStaticVehiclesInfo() {
        List<Vehicle> vehicles = this.vehicleRepository.findAll();
        return VehicleDTO.convert(vehicles);
    }

    public VehicleService(VehicleRepository vehicleRepository, VehicleRideRepository vehicleRideRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleRideRepository = vehicleRideRepository;
    }

    public VehicleDTO getVehicleInfo(String id) {
        VehicleDTO vehicleDTO = null;
        Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            vehicleDTO = new VehicleDTO(vehicle.get());
            Optional<VehicleRide> vehicleRide = this.vehicleRideRepository.findByVehicleIdAndEndDate(id, null);
            if (vehicleRide.isPresent()) {
                VehicleRide v = vehicleRide.get();
                vehicleDTO.setAvailableSeats(v.getAvailableSeats());
                vehicleDTO.setLocation(v.getLocation());
                vehicleDTO.setStartDate(v.getStartDate());
                vehicleDTO.setEndDate(v.getEndDate());
                vehicleDTO.setRoute(v.getRoute());
                vehicleDTO.setPickPoint(v.getPickPoints());
            }
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
}
