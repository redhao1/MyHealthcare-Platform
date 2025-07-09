package com.mhp.appointment_service.controller;

import com.mhp.appointment_service.dto.AppointmentRequestDTO;
import com.mhp.appointment_service.dto.AppointmentResponseDTO;
import com.mhp.appointment_service.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointment")
@Tag(name = "Appointment", description = "API for managing appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointments();

        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/patient/{id}")
    @Operation(summary = "Get all appointments of patient")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsOfPatient(@PathVariable UUID id) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsOfPatient(id);

        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/doctor/{id}")
    @Operation(summary = "Get all appointments of doctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsOfDoctor(@PathVariable UUID id) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsOfDoctor(id);

        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one appointment by id")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@PathVariable UUID id){
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.getAppointment(id);
        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO) {

        AppointmentResponseDTO appointmentResponseDTO = appointmentService.createAppointment(appointmentRequestDTO);

        return ResponseEntity.ok().body(appointmentResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the info of a appointment")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO) {

        AppointmentResponseDTO appointmentResponseDTO = appointmentService.
                updateAppointment(id, appointmentRequestDTO);

        return ResponseEntity.ok().body(appointmentResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
