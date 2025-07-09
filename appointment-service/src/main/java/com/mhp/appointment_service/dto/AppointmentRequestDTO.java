package com.mhp.appointment_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequestDTO {

    @NotBlank(message = "patient id is required")
    private String patientId;
    @NotBlank(message = "doctor id is required")
    private String doctorId;
    @NotBlank(message = "appointment time is required")
    private String appointmentTime;
    private String reason;
}
