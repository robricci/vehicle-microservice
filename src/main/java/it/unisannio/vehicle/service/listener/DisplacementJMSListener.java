package it.unisannio.vehicle.service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.vehicle.dto.internal.VehicleDisplacementDTO;
import it.unisannio.vehicle.service.MovingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class DisplacementJMSListener {

    private ObjectMapper objectMapper;
    private MovingService movingService;

    @Autowired
    public DisplacementJMSListener(ObjectMapper objectMapper, MovingService movingService) {
        this.objectMapper = objectMapper;
        this.movingService = movingService;
    }

    @JmsListener(destination = "${jms.topic.vehicle-displacement}")
    private void receive(Message message) throws JMSException, JsonProcessingException {
        if (!(message instanceof TextMessage)) throw new IllegalArgumentException("Message must be of type TextMessage");
        VehicleDisplacementDTO vehicleDisplacement;
        try {
            String json = ((TextMessage) message).getText();
            vehicleDisplacement = objectMapper.readValue(json, VehicleDisplacementDTO.class);
        } catch (JMSException | JsonMappingException ex) {
            throw new RuntimeException(ex);
        }

        this.movingService.sendDisplacementNotification(vehicleDisplacement);
    }
}
