package it.unisannio.vehicle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@EnableJms
public class ArtemisService {

    @Value("${jms.topic.trip-confirmation}")
    private String tripConfirmationTopic;

    private JmsTemplate jmsTemplate;

    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String topic, Object obj){
        jmsTemplate.convertAndSend(topic, obj);
    }

    public String getTripConfirmationTopic() {
        return tripConfirmationTopic;
    }
}
