package it.unisannio.vehicle.repository;

import it.unisannio.vehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    @Query("{ occupiedSeats: 0 }")
    List<Vehicle> getAllStoppedVehicles();

    @Query("{ occupiedSeats: 0, licensePlate: { $in: ?0 } }")
    List<Vehicle> getAllStoppedVehiclesWithLicensePlate(List<String> licensePlates);

    @Query(value = "{ $where: 'this.occupiedSeats < this.totalAvailableSeats', 'ride.route.nodeId': { $all: ?0 }}",
            sort = "{ 'occupiedSeats': -1 }")
    List<Vehicle> findByNodeIdsInRouteAndAvailableSeatsAndOrderByOccupiedSeatsDesc(List<Integer> ids);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    @Query(value = "{ licensePlate: ?0, occupiedSeats: ?1 }", delete = true)
    Optional<Vehicle> findByLicensePlateAndOccupiedSeatsAndRemove(String licensePlate, int occupiedSeats);
}
