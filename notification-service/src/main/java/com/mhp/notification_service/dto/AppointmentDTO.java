package com.mhp.notification_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentDTO {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String reason;
    private LocalDateTime appointmentDate;

}
