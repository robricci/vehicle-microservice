package it.unisannio.vehicle.websocket;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@ServerEndpoint(value = "/ws/manage/notifications/", encoders = {ManageNotificationEncoder.class})
public class WebSocketEndpoint {
    public static Map<String, Session> peers = Collections.synchronizedMap(new HashMap<String, Session>());

    @OnOpen
    public void start(Session session) {
        peers.put(session.getId(), session);
        System.out.println("A connection has been established");
    }
   
    @OnClose
    public void end(Session session) {
    	peers.remove(session.getId());
    }

    @OnMessage
    public void receive(String message) {
        // in this example we don't receive messages with this endpoint
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }

    public void send(Object obj, String id) {
        if (peers.get(id) != null) {
            peers.get(id).getAsyncRemote().sendObject(obj);
        }
    }
}
