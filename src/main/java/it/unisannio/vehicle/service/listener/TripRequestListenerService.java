package it.unisannio.vehicle.service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.vehicle.dto.internal.TripDTO;
import it.unisannio.vehicle.dto.internal.TripNotificationDTO;
import it.unisannio.vehicle.pojo.VehicleStatus;
import it.unisannio.vehicle.service.MovingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class TripRequestListenerService {

    private ObjectMapper objectMapper;
    private MovingService movingService;

    @Autowired
    public TripRequestListenerService(ObjectMapper objectMapper, MovingService movingService) {
        this.objectMapper = objectMapper;
        this.movingService = movingService;
    }

    @JmsListener(destination = "${jms.topic.trip-request}")
    private void receive(Message message) throws JMSException, JsonProcessingException {
        if (!(message instanceof TextMessage)) throw new IllegalArgumentException("Message must be of type TextMessage");
        TripDTO tripDTO;
        try {
            String json = ((TextMessage) message).getText();
            tripDTO = objectMapper.readValue(json, TripDTO.class);
        } catch (JMSException | JsonMappingException ex) {
            throw new RuntimeException(ex);
        }

        VehicleStatus vehicleStatus = this.movingService.requestAssignment(tripDTO);
        switch (vehicleStatus.getStatus()) {
            case READY_TO_START:
                // we can send a TripNotification for each request (PickPoints of vehicle)
                this.movingService.startRideForVehicleAndSendTripNotification(vehicleStatus.getVehicle());
                break;
            case MOVING:
                // we can send a TripNotification only for last trip
                this.movingService.sendTripNotification(
                        tripDTO.getId(),
                        TripNotificationDTO.Status.APPROVED,
                        vehicleStatus.getVehicle().getLicensePlate(),
                        tripDTO.getSource());
                break;
            case FULL:
                this.movingService.sendTripNotification(tripDTO.getId(), TripNotificationDTO.Status.REJECTED, null, null);
                break;
            default:
                // nothing;
        }
    }
}
