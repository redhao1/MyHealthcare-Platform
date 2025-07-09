package com.mhp.appointment_service.kafka;

import com.mhp.appointment_service.model.Appointment;
import com.mhp.appointment_service.repository.AppointmentRepository;
import org.apache.kafka.shaded.com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    AppointmentRepository appointmentRepository;
    @KafkaListener(topics = "appointment-create-compensation", groupId = "appointment_service")
    public void consumeEvent(Appointment event) {
        handleCompensation(event);
    }
    public void handleCompensation(Appointment event) {
        try {
            if (appointmentRepository.existsById(event.getId())) {
                appointmentRepository.deleteById(event.getId());
                log.info("Rolled back appointment {}", event.getId());
            } else {
                log.info("Nothing to roll back for {}", event.getId());
            }
        } catch (Exception e) {
            log.error("Error deserializing event: {}", e.getMessage(), e);
        }
    }
}
