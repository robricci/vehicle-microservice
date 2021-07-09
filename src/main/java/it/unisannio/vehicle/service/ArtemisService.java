package it.unisannio.vehicle.service;

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

@Service
@EnableJms
public class ArtemisService {

    @Value("${jms.topic.trip-confirmation}")
    private String tripConfirmationTopic;

    private JmsTemplate jmsTemplate;
    private VehicleService vehicleService;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, VehicleService vehicleService) {
        this.jmsTemplate = jmsTemplate;
        this.vehicleService = vehicleService;
    }

    public void sendTripNotification(TripNotificationDTO tripNotification){
        jmsTemplate.convertAndSend(tripConfirmationTopic, tripNotification);
    }

    @JmsListener(destination = "${jms.topic.trip-request}")
    private void receive(Message message) throws JMSException {
        // TODO getBody: error decoding
        TripDTO tripDTO = message.getBody(TripDTO.class);
        TripNotificationDTO tripNotification;

        VehicleStatus vehicleStatus = this.vehicleService.requestAssignment(tripDTO);
        switch (vehicleStatus.getStatus()) {
            case READY_TO_START:
                // we can send a TripNotification for each request (PickPoints of vehicle)
                for (PickPoint pickPoint : vehicleStatus.getVehicle().getRide().getPickPoints()) {
                    tripNotification = new TripNotificationDTO();
                    tripNotification.setTripId(pickPoint.getTripId());
                    tripNotification.setVehicleLicenseId(vehicleStatus.getVehicle().getLicensePlate());
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
                tripNotification.setVehicleLicenseId(vehicleStatus.getVehicle().getLicensePlate());
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
