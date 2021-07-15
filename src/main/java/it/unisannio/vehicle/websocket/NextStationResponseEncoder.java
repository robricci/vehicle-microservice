package it.unisannio.vehicle.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.vehicle.dto.NextStationDTO;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class NextStationResponseEncoder implements Encoder.Text<NextStationDTO>{

	  @Override
	  public String encode(NextStationDTO trip) throws EncodeException {
      
	    String jsonString = null;
		try {
	      jsonString =  new ObjectMapper().writeValueAsString(trip);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	  }

	  @Override
	  public void init(EndpointConfig ec) {
	    System.out.println("NextStationResponseEncoder - init method called");
	  }

	  @Override
	  public void destroy() {
	    System.out.println("NextStationResponseEncoder - destroy method called");
	  }
}
