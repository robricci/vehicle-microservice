package it.unisannio.vehicle.service;

import it.unisannio.vehicle.model.Websocket;
import it.unisannio.vehicle.repository.WebsocketRepository;
import it.unisannio.vehicle.websocket.WebSocketClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WebSocketService {

    @Value("${server.instance-id}")
    private String instanceId;

    private static Map<String, Session> peers = new HashMap<>();

    private WebsocketRepository websocketRepository;

    @Autowired
    public WebSocketService(WebsocketRepository websocketRepository) {
        this.websocketRepository = websocketRepository;
    }

    public void addPeer(Session session, String licensePlate) {
        this.websocketRepository.save(new Websocket(preAppendInstanceToId(session.getId()), licensePlate));
        peers.put(preAppendInstanceToId(session.getId()), session);
    }

    public void removePeer(String sessionId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionIdAndRemove(preAppendInstanceToId(sessionId));
        websocket.ifPresent(value -> peers.remove(value.getInstanceSessionId()));
    }

    public void sendMessage(String licensePlate, Object msg) throws WebSocketClientNotFoundException {
        Optional<Websocket> websocket = this.websocketRepository.findByLicensePlate(licensePlate);
        if (websocket.isEmpty())
            throw new WebSocketClientNotFoundException("WS identified by licensePlate: <" + licensePlate + "> is not present");

        peers.get(websocket.get().getInstanceSessionId()).getAsyncRemote().sendObject(msg);
    }

    public String getLicensePlateFromWebsocket(String sessionId) {
        Optional<Websocket> websocket = this.websocketRepository.findByInstanceSessionId(preAppendInstanceToId(sessionId));
        return websocket.map(Websocket::getLicensePlate).orElse(null);
    }

    private String preAppendInstanceToId(String id) {
        return instanceId.concat("@").concat(id);
    }
}
