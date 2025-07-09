package com.mhp.doctor_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentEvent {
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime appointmentTime;
    private String resaon;
}
