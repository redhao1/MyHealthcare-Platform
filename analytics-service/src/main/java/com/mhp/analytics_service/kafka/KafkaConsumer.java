package com.mhp.analytics_service.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    KafkaProducer kafkaProducer;
    @KafkaListener(topics = "patient", groupId = "analytics_service")
    public void consumeEvent(byte[] event) {

        CompletableFuture.runAsync(() -> processPatient(event));

    }
    public void processPatient(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            // ... perform any business logic related to analytices here
            Thread.sleep(2000);

            log.info("Received Patient Event: [PatientId={}, PatientName={}, PatientEmail={}]",
                    patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail() );
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event: {}", e.getMessage());
            kafkaProducer.sendGrpcEvent(event, "patient-create-compensation");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
