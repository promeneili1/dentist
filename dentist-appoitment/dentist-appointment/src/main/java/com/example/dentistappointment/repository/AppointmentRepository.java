package com.example.dentistappointment.repository;

import com.example.dentistappointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientJMBG(String patientJMBG);

    @Query("SELECT a FROM Appointment a WHERE (:startTime BETWEEN a.startTime AND a.endTime OR :endTime BETWEEN a.startTime AND a.endTime)")
    List<Appointment> findOverlappingAppointments(LocalDateTime startTime, LocalDateTime endTime);


    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
