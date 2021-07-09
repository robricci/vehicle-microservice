package it.unisannio.vehicle.websocket;

public class WebSocketClientNotFoundException extends Exception {

    public WebSocketClientNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
