package com.mhp.appointment_service.kafka;

import com.mhp.appointment_service.model.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStringEvent(Appointment appointment, String topic) {
        try {
            kafkaTemplate.send(topic, appointment);
        } catch (Exception e) {
            log.error("Error sending appointment Created event: {}", appointment.toString(), e);
            throw new RuntimeException("Kafka send failed", e);
        }
    }
}
