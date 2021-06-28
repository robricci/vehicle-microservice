package it.unisannio.vehicle.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class ManageNotificationEncoder implements Encoder.Text<Object>{

	  @Override
	  public String encode(Object trip) throws EncodeException {
      
	    String jsonString = null;
		try {  
	      jsonString =  new ObjectMapper().writeValueAsString(trip);
		} catch (Exception e) {}
		return jsonString;
	  }

	  @Override
	  public void init(EndpointConfig ec) {
	    System.out.println("MessageEncoder - init method called");
	  }

	  @Override
	  public void destroy() {
	    System.out.println("MessageEncoder - destroy method called");
	  }
}
