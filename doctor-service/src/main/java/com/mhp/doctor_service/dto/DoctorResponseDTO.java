package com.mhp.doctor_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponseDTO {
    private String id;
    private String name;
    private String email;
    private String department;
    private String address;
    private String dateOfBirth;
}
