package com.mhp.notification_service.kafka;

import com.mhp.notification_service.dto.AppointmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFallbackEvent(AppointmentDTO appointment, String topic) {
        try {
            kafkaTemplate.send(topic, appointment);
        } catch (Exception e) {
            log.error("Error sending appointment create compensation event: {}", appointment.toString(), e);
            throw new RuntimeException("Kafka event send failed", e);
        }
    }
}
