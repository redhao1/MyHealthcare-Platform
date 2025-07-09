package com.mhp.notification_service.kafka;

import com.mhp.notification_service.dto.AppointmentDTO;
import com.mhp.notification_service.feignClient.*;
import com.mhp.notification_service.service.NotificationFactory;
import com.mhp.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaConsumer {

    private final NotificationFactory notificationFactory;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(NotificationFactory factory,
                         PatientClient patientClient,
                         DoctorClient doctorClient,
                         KafkaProducer kafkaProducer) {
        this.notificationFactory = factory;
        this.patientClient = patientClient;
        this.doctorClient = doctorClient;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "appointment", groupId = "notification-group")
    public void listen(AppointmentDTO event) {

        // Validate patient and doctor with multithreading

        CompletableFuture<ResponseEntity<Map<String, Object>>> patientFuture =
                CompletableFuture.supplyAsync(() -> patientClient.getPatientById(event.getPatientId()));

        CompletableFuture<ResponseEntity<Map<String, Object>>> doctorFuture =
                CompletableFuture.supplyAsync(() -> doctorClient.getDoctorById(event.getDoctorId()));

        patientFuture.thenCombine(doctorFuture, (patientResponse, doctorResponse) -> {

            if (!patientResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Invalid patient ID");
            }
            if (!doctorResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Invalid doctor ID");
            }

            NotificationService emailService = notificationFactory.getService("email");
            NotificationService smsService = notificationFactory.getService("sms");

            if (patientResponse.getBody() != null) {
                String patientName = patientResponse.getBody().get("name").toString();
                String patientEmail = patientResponse.getBody().get("email").toString();
                emailService.sendNotification(patientEmail, patientName, event.getReason(), event.getAppointmentDate());
                smsService.sendNotification(patientEmail, patientName, event.getReason(), event.getAppointmentDate());
            }

            if (doctorResponse.getBody() != null) {
                String doctorName = doctorResponse.getBody().get("name").toString();
                String doctorEmail = doctorResponse.getBody().get("email").toString();
                emailService.sendNotification(doctorEmail, doctorName, event.getReason(), event.getAppointmentDate());
                smsService.sendNotification(doctorEmail, doctorName, event.getReason(), event.getAppointmentDate());
            }
            return null;
        }).exceptionally(ex -> {
            log.error("Failed to process appointment event: {}, need to send fallback event", ex.getMessage(), ex);
            kafkaProducer.sendFallbackEvent(event, "appointment-create-compensation");
            return null;
        });
    }
}
