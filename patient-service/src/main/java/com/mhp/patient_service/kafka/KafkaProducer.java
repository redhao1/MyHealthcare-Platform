package com.mhp.patient_service.kafka;

import com.mhp.patient_service.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {

        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATE")
                .build();

        try {
            kafkaTemplate.send("patient", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending Patient Created event: {}", event);
        }
    }
}
