package com.mhp.appointment_service.service;

import com.mhp.appointment_service.dto.AppointmentRequestDTO;
import com.mhp.appointment_service.dto.AppointmentResponseDTO;
import com.mhp.appointment_service.exception.AppointmentNotFoundException;
import com.mhp.appointment_service.exception.DateTimeAlreadyExistsException;
import com.mhp.appointment_service.feignClient.DoctorClient;
import com.mhp.appointment_service.feignClient.PatientClient;
import com.mhp.appointment_service.kafka.KafkaProducer;
import com.mhp.appointment_service.mapper.AppointmentMapper;
import com.mhp.appointment_service.model.Appointment;
import com.mhp.appointment_service.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AppointmentService {

    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final AppointmentRepository appointmentRepository;
    private final KafkaProducer kafkaProducer;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientClient patientClient, DoctorClient doctorClient,
                              KafkaProducer kafkaProducer) {
        this.appointmentRepository = appointmentRepository;
        this.patientClient = patientClient;
        this.doctorClient = doctorClient;
        this.kafkaProducer = kafkaProducer;
    }

    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {
        // Validate patient
        ResponseEntity<Map<String, Object>> patientResponse = patientClient.getPatientById(
                UUID.fromString(request.getPatientId()));
        if (!patientResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Invalid patient ID");
        }

        // Validate doctor
        ResponseEntity<Map<String, Object>> doctorResponse =
                doctorClient.getDoctorById(UUID.fromString(request.getDoctorId()));
        if (!doctorResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Invalid doctor ID");
        }

        log.info("patientResponse: {}, doctorResponse; {}", patientResponse, doctorResponse);

        // Save appointment
        Appointment appointment = AppointmentMapper.toAppointment(request);
        appointmentRepository.save(appointment);
        kafkaProducer.sendStringEvent(appointment, "appointment");


        return AppointmentMapper.toDTO(appointment);
    }

    public List<AppointmentResponseDTO> getAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        List<AppointmentResponseDTO> appointmentResponseDTOS = appointments.stream()
                .map(AppointmentMapper::toDTO).toList();
        return appointmentResponseDTOS;
    }

    public List<AppointmentResponseDTO> getAppointmentsOfPatient(UUID patientId) {
        List<Appointment> appointments = appointmentRepository.findAllByPatientId(patientId);

        List<AppointmentResponseDTO> appointmentResponseDTOS = appointments.stream()
                .map(AppointmentMapper::toDTO).toList();
        return appointmentResponseDTOS;
    }

    public List<AppointmentResponseDTO> getAppointmentsOfDoctor(UUID doctorId) {
        List<Appointment> appointments = appointmentRepository.findAllByDoctorId(doctorId);

        List<AppointmentResponseDTO> appointmentResponseDTOS = appointments.stream()
                .map(AppointmentMapper::toDTO).toList();
        return appointmentResponseDTOS;
    }

    public AppointmentResponseDTO getAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppointmentNotFoundException("Appointment not found with ID: " + id));

        return AppointmentMapper.toDTO(appointment);
    }

    public AppointmentResponseDTO updateAppointment(UUID id, AppointmentRequestDTO appointmentRequestDTO) {

        // check if the appointment with input id exist in the database
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppointmentNotFoundException("Appointment not found with ID: " + id));

        LocalDateTime newTime = LocalDateTime.parse(appointmentRequestDTO.getAppointmentTime());
        UUID newDoctorId = UUID.fromString(appointmentRequestDTO.getDoctorId());

        // check if the new email matches with any other existing email in the database
        if (appointmentRepository.existsByAppointmentTimeWithDoctorIdAndIdNot(newTime, newDoctorId, id)) {
            throw new DateTimeAlreadyExistsException(
                    "Another appointment with this doctor at this time " + "already exists");
        }

        appointment = AppointmentMapper.toAppointment(appointmentRequestDTO);

        appointmentRepository.save(appointment);

        return AppointmentMapper.toDTO(appointment);
    }

    public void deleteAppointment(UUID id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppointmentNotFoundException("Appointment not found with ID: " + id));

        // may also need to check other service before deleting it from database
        appointmentRepository.deleteById(id);
    }
}
