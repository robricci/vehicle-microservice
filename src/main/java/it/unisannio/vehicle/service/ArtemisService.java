package it.unisannio.vehicle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.TripDTO;
import it.unisannio.vehicle.dto.internal.TripNotificationDTO;
import it.unisannio.vehicle.pojo.VehicleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
@EnableJms
public class ArtemisService {

    @Value("${jms.topic.trip-confirmation}")
    private String tripConfirmationTopic;

    private JmsTemplate jmsTemplate;
    private VehicleService vehicleService;
    private ObjectMapper objectMapper;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, VehicleService vehicleService, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.vehicleService = vehicleService;
        this.objectMapper = objectMapper;
    }

    public void sendTripNotification(TripNotificationDTO tripNotification){
        jmsTemplate.convertAndSend(tripConfirmationTopic, tripNotification);
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

        TripNotificationDTO tripNotification;

        VehicleStatus vehicleStatus = this.vehicleService.requestAssignment(tripDTO);
        switch (vehicleStatus.getStatus()) {
            case READY_TO_START:
                // we can send a TripNotification for each request (PickPoints of vehicle)
                for (PickPoint pickPoint : vehicleStatus.getVehicle().getRide().getPickPoints()) {
                    tripNotification = new TripNotificationDTO();
                    tripNotification.setTripId(pickPoint.getTripId());
                    tripNotification.setVehicleLicensePlate(vehicleStatus.getVehicle().getLicensePlate());
                    tripNotification.setPickUpNodeId(pickPoint.getSourceNodeId());
                    tripNotification.setStatus(TripNotificationDTO.Status.APPROVED);
                    this.sendTripNotification(tripNotification);
                }

                this.vehicleService.startRideForVehicle(vehicleStatus.getVehicle());
                break;
            case MOVING:
                // we can send a TripNotification only for last trip
                tripNotification = new TripNotificationDTO();
                tripNotification.setTripId(tripDTO.getId());
                tripNotification.setVehicleLicensePlate(vehicleStatus.getVehicle().getLicensePlate());
                tripNotification.setPickUpNodeId(tripDTO.getSource());
                tripNotification.setStatus(TripNotificationDTO.Status.APPROVED);
                this.sendTripNotification(tripNotification);
                break;
            case FULL:
                this.sendTripNotification(new TripNotificationDTO(tripDTO.getId(), TripNotificationDTO.Status.REJECTED));
                break;
            default:
                // nothing;
        }
    }
}
