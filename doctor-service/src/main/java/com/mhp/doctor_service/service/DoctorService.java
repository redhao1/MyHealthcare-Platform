package com.mhp.doctor_service.service;

import com.mhp.doctor_service.dto.DoctorRequestDTO;
import com.mhp.doctor_service.dto.DoctorResponseDTO;
import com.mhp.doctor_service.exception.*;
import com.mhp.doctor_service.mapper.DoctorMapper;
import com.mhp.doctor_service.model.Doctor;
import com.mhp.doctor_service.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorResponseDTO> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorResponseDTO> doctorResponseDTOS = doctors.stream()
                .map(DoctorMapper::toDTO).toList();
        return doctorResponseDTOS;
    }

    public DoctorResponseDTO getDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                () -> new DoctorNotFoundException("Doctor not found with ID: " + id));

        return DoctorMapper.toDTO(doctor);
    }

    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {

        if(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A doctor with this email " +
                    "already exists: " + doctorRequestDTO.getEmail());
        }
        Doctor newDoctor = doctorRepository.save(
                DoctorMapper.toDoctor(doctorRequestDTO));

//        kafkaProducer.sendGrpcEvent(newDoctor, "doctor");

        return DoctorMapper.toDTO(newDoctor);
    }

    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO doctorRequestDTO) {

        // check if the doctor with input id exist in the database
        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                () -> new DoctorNotFoundException("Doctor not found with ID: " + id));

        String newEmail = doctorRequestDTO.getEmail();

        // check if the new email matches with any other existing email in the database
        if(doctorRepository.existsByEmailAndIdNot(newEmail, id)) {
            throw new EmailAlreadyExistsException(
                    "Another doctor with this email " + "already exists: "
                            + doctorRequestDTO.getEmail());
        }

        doctor.setName(doctorRequestDTO.getName());
        doctor.setAddress(doctorRequestDTO.getAddress());
        doctor.setEmail(newEmail);
        doctor.setDepartment(doctorRequestDTO.getDepartment());
        doctor.setDateOfBirth(LocalDate.parse(doctorRequestDTO.getDateOfBirth()));

        doctorRepository.save(doctor);
        return DoctorMapper.toDTO(doctor);
    }

    public void deleteDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                () -> new DoctorNotFoundException("Doctor not found with ID: " + id));

        // may also need to check other service like appointment service before deleting it from database
        doctorRepository.deleteById(id);

    }

}
