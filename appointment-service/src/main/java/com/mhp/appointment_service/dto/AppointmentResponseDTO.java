package com.mhp.appointment_service.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentResponseDTO {

    private String id;
    private String patientId;
    private String doctorId;
    private String appointmentDate;
    private String reason;
}
