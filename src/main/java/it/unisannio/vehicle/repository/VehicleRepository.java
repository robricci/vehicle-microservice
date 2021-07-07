package it.unisannio.vehicle.repository;

import it.unisannio.vehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    @Query("{ occupiedSeats: 0 }")
    List<Vehicle> getAllStoppedVehicles();

    @Query("{ 'ride.route.nodeId': { $in: ?0 } }")
    List<Vehicle> getVehiclesInRouteWithNodeIds(List<Integer> ids);
}
