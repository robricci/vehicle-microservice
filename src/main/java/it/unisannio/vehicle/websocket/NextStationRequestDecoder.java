package it.unisannio.vehicle.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.vehicle.dto.NextStationRequestDTO;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;


public class NextStationRequestDecoder implements Decoder.Text<NextStationRequestDTO> {

    @Override
    public NextStationRequestDTO decode(String tripString) throws DecodeException {

		ObjectMapper mapper = new ObjectMapper();
        NextStationRequestDTO nextStationRequestDTO = null;
		try {
            nextStationRequestDTO = mapper.readValue(tripString, NextStationRequestDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return nextStationRequestDTO;
    }

    @Override
    public boolean willDecode(String tripString) {
        return tripString != null;
    }

    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }
}
