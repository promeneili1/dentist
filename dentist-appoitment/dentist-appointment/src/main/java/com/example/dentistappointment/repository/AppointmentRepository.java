package com.example.dentistappointment.repository;

import com.example.dentistappointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientJMBG(String patientJMBG);
    /*List<Appointment> findByPatientId(String id);*/

    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
