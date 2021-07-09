package it.unisannio.vehicle.websocket;

import it.unisannio.vehicle.Utils;
import it.unisannio.vehicle.dto.NextStationDTO;
import it.unisannio.vehicle.dto.NextStationRequestDTO;
import it.unisannio.vehicle.service.VehicleService;
import it.unisannio.vehicle.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@ServerEndpoint(value = "/api/drivers", decoders = { NextStationRequestDecoder.class })
public class WebSocketEndpoint {

    private final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private static VehicleService vehicleService;
    private static WebSocketService webSocketService;

    @Autowired
    public void setVehicleService(VehicleService vehicleService) {
        WebSocketEndpoint.vehicleService = vehicleService;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        WebSocketEndpoint.webSocketService = webSocketService;
    }

    @OnOpen
    public void start(Session session) throws Exception {
        Map<String, String> queryMap = Utils.buildQueryMap(session.getQueryString());
        if (queryMap != null && queryMap.get("licensePlate") != null) {
            webSocketService.addPeer(session, queryMap.get("licensePlate"));
            logger.info("A WS connection has been established");
        } else
            logger.info("licensePlate not found in websocket query string");
            throw new Exception();
    }
   
    @OnClose
    public void end(Session session) {
        webSocketService.removePeer(session.getId());
    }

    @OnMessage
    public void receive(Session session, NextStationRequestDTO request) {
        NextStationDTO nextStation = vehicleService.getNextStation(session.getId(), request);
        session.getAsyncRemote().sendObject(nextStation);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }
}
