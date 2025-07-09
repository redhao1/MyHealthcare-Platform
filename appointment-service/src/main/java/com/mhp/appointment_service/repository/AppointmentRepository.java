package com.mhp.appointment_service.repository;

import com.mhp.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    @Query("select (count(a) > 0) from Appointment a where a.appointmentDate = ?1 and a.doctorId = ?2 and a.id <> ?3")
    boolean existsByAppointmentTimeWithDoctorIdAndIdNot(LocalDateTime appointmentDate, UUID doctorId, UUID id);

    List<Appointment> findAllByPatientId(UUID patientId);
    List<Appointment> findAllByDoctorId(UUID doctorId);
}
