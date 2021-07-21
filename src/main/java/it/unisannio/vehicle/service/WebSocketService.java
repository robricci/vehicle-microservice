package it.unisannio.vehicle.service;

import it.unisannio.vehicle.model.Websocket;
import it.unisannio.vehicle.repository.WebsocketRepository;
import it.unisannio.vehicle.websocket.WebSocketClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private static final String instanceId = initializeInstanceId();

    private static Map<String, Session> peers = new HashMap<>();

    private WebsocketRepository websocketRepository;

    @Autowired
    public WebSocketService(WebsocketRepository websocketRepository) {
        this.websocketRepository = websocketRepository;
    }

    public void addPeer(Session session, String licensePlate) {
        String id = preAppendInstanceToId(session.getId());
        this.websocketRepository.save(new Websocket(id, licensePlate));
        peers.put(id, session);
        logger.info("New websocket opened: " + id);

    }

    public void removePeer(String sessionId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionIdAndRemove(preAppendInstanceToId(sessionId));
        websocket.ifPresent(value -> {
            peers.remove(value.getInstanceSessionId());
            logger.info("Websocket removed: " + value.getInstanceSessionId());
        });
    }

    public void sendMessage(String licensePlate, Object msg) throws WebSocketClientNotFoundException {
        Optional<Websocket> websocket = this.websocketRepository.findByLicensePlate(licensePlate);
        if (websocket.isEmpty())
            throw new WebSocketClientNotFoundException("WS identified by licensePlate: <" + licensePlate + "> is not present");

        peers.get(websocket.get().getInstanceSessionId()).getAsyncRemote().sendObject(msg);
        logger.info("Message sent to websocket: " + websocket.get().getInstanceSessionId());
    }

    public String getLicensePlateFromWebsocket(String sessionId) {
        Optional<Websocket> websocket = this.websocketRepository.findByInstanceSessionId(preAppendInstanceToId(sessionId));
        return websocket.map(Websocket::getLicensePlate).orElse(null);
    }

    private String preAppendInstanceToId(String id) {
        return instanceId.concat("@").concat(id);
    }

    private static String initializeInstanceId() {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        String instanceId = String.valueOf(t.getTime());
        logger.info("Instance ID: " + instanceId);
        return instanceId;
    }
}
