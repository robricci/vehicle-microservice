package it.unisannio.vehicle.repository;

import it.unisannio.vehicle.model.VehicleRide;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;


@Repository
public interface VehicleRideRepository extends MongoRepository<VehicleRide, String> {

    public Optional<VehicleRide> findByVehicleIdAndEndDate(String vehicleId, Date endDate);
}
