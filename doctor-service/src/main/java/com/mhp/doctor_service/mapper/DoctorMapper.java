package com.mhp.doctor_service.mapper;

import com.mhp.doctor_service.dto.DoctorRequestDTO;
import com.mhp.doctor_service.dto.DoctorResponseDTO;
import com.mhp.doctor_service.model.Doctor;

import java.time.LocalDate;

public class DoctorMapper {
    public static DoctorResponseDTO toDTO(Doctor doctor) {
        DoctorResponseDTO doctorDTO = new DoctorResponseDTO();
        doctorDTO.setId(doctor.getId().toString());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setAddress(doctor.getAddress());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setDepartment(doctor.getDepartment());
        doctorDTO.setDateOfBirth(doctor.getDateOfBirth().toString());

        return doctorDTO;
    }

    public static Doctor toDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorRequestDTO.getName());
        doctor.setAddress(doctorRequestDTO.getAddress());
        doctor.setEmail(doctorRequestDTO.getEmail());
        doctor.setDepartment(doctorRequestDTO.getDepartment());
        doctor.setDateOfBirth(LocalDate.parse(doctorRequestDTO.getDateOfBirth()));

        return doctor;
    }
}
