package com.mhp.doctor_service.controller;

import com.mhp.doctor_service.dto.DoctorRequestDTO;
import com.mhp.doctor_service.dto.DoctorResponseDTO;
import com.mhp.doctor_service.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor")
@Tag(name = "Doctor", description = "API for managing doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // endpoint to get all doctors, return as a List of DoctorResponseDTO
    @GetMapping("/all")
    @Operation(summary = "Get all doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getDoctors();

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    // endpoint to get a doctor by id, return as a DoctorResponseDTO
    @GetMapping("/{id}")
    @Operation(summary = "Get a doctor by id")
    public ResponseEntity<DoctorResponseDTO> getDoctor(@PathVariable UUID id) {
        DoctorResponseDTO doctor = doctorService.getDoctor(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new doctor")
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {

        DoctorResponseDTO doctorResponseDTO = doctorService.createDoctor(doctorRequestDTO);

        return ResponseEntity.ok().body(doctorResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the info of a doctor")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {

        DoctorResponseDTO doctorResponseDTO = doctorService.
                updateDoctor(id, doctorRequestDTO);

        return ResponseEntity.ok().body(doctorResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
