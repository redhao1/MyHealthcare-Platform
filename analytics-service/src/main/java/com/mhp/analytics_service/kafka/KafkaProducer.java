package com.mhp.analytics_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGrpcEvent(byte[] message, String topic) {
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", message);
        }
    }

    public void sendStringEvent(Object message, String topic) {
        try {
            kafkaTemplate.send(topic, message.toString());
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", message.toString());
        }
    }
}
