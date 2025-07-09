package com.mhp.appointment_service.mapper;

import com.mhp.appointment_service.dto.AppointmentRequestDTO;
import com.mhp.appointment_service.dto.AppointmentResponseDTO;
import com.mhp.appointment_service.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentMapper {
    public static AppointmentResponseDTO toDTO(Appointment appointment) {
        AppointmentResponseDTO appointmentDTO = new AppointmentResponseDTO();
        appointmentDTO.setId(appointment.getId().toString());
        appointmentDTO.setPatientId(appointment.getPatientId().toString());
        appointmentDTO.setDoctorId(appointment.getDoctorId().toString());
        appointmentDTO.setAppointmentDate(appointment.getAppointmentDate().toString());
        appointmentDTO.setReason(appointment.getReason());

        return appointmentDTO;
    }

    public static Appointment toAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(UUID.fromString(appointmentRequestDTO.getPatientId()));
        appointment.setDoctorId(UUID.fromString(appointmentRequestDTO.getDoctorId()));
        appointment.setAppointmentDate(LocalDateTime.parse(appointmentRequestDTO.getAppointmentTime()));
        appointment.setReason(appointmentRequestDTO.getReason());

        return appointment;
    }
}
