package com.mhp.patient_service.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mhp.patient_service.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    PatientRepository patientRepository;
    @KafkaListener(topics = "patient-create-compensation", groupId = "patient_service")
    public void consumeEvent(byte[] event) {

        handleCompensation(event);

    }
    public void handleCompensation(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            if (patientRepository.existsByEmail(patientEvent.getEmail())) {
                patientRepository.deleteById(UUID.fromString(patientEvent.getPatientId()));
                log.info("Rolled back patient {}", patientEvent.getPatientId());
            } else {
                log.info("Nothing to roll back for {}", patientEvent.getPatientId());
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event: {}", e.getMessage(), e);
        }
    }
}
