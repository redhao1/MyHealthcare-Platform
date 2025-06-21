package com.mhp.patient_service.service;

import com.mhp.patient_service.dto.PatientRequestDTO;
import com.mhp.patient_service.dto.PatientResponseDTO;
import com.mhp.patient_service.mapper.PatientMapper;
import com.mhp.patient_service.model.Patient;
import com.mhp.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOS = patients.stream()
                .map(PatientMapper::toDTO).toList();
        return patientResponseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        Patient newPatient = patientRepository.save(
                PatientMapper.toPatient(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }
}
