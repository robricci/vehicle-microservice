package it.unisannio.vehicle.repository;

import it.unisannio.vehicle.model.Websocket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebsocketRepository extends MongoRepository<Websocket, String> {

    @Query(value = "{ instanceSessionId: ?0 }", delete = true)
    Optional<Websocket> findByInstanceSessionIdAndRemove(String sessionId);

    Optional<Websocket> findByLicensePlate(String licensePlate);

    Optional<Websocket> findByInstanceSessionId(String instanceSessionId);
}
