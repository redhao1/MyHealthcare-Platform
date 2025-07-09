package com.mhp.patient_service.kafka;

import com.mhp.patient_service.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGrpcEvent(Patient patient, String topic) {

        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATE")
                .build();

        try {
            kafkaTemplate.send(topic, event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", event);
        }
    }

    public void sendStringEvent(Patient patient, String topic) {
        try {
            kafkaTemplate.send(topic, patient.toString());
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", patient.toString());
        }
    }
}
