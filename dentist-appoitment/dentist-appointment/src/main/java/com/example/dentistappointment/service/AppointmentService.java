package com.example.dentistappointment.service;

import com.example.dentistappointment.model.Appointment;
import com.example.dentistappointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;


    @Value("${appointment.cancellation.deadline.hours:24}")
    private int cancellationDeadlineHours;



    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAppointmentsByJMBG(String jmbg) {
        return appointmentRepository.findByPatientJMBG(jmbg);
    }

    public Optional<Appointment> findAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> findAppointmentsByPatientJMBG(String patientJMBG) {
        return appointmentRepository.findByPatientJMBG(patientJMBG);
    }

    public boolean isOverlappingAppointment(Appointment appointment) {
        LocalDateTime startOfDay = appointment.getStartTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Appointment> appointmentsForDay = appointmentRepository.findByStartTimeBetween(startOfDay, endOfDay);

        for (Appointment existingAppointment : appointmentsForDay) {
            if (appointmentsOverlap(existingAppointment, appointment)) {
                return true;
            }
        }
        return false;
    }

    private boolean appointmentsOverlap(Appointment a1, Appointment a2) {
        return a1.getStartTime().isBefore(a2.getEndTime()) && a1.getEndTime().isAfter(a2.getStartTime());
    }


    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    /*public List<Appointment> findAppointmentsByPatientId(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }*/

    public List<Appointment> findAppointmentsByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByStartTimeBetween(start, end);
    }

    public Appointment saveAppointment(Appointment appointment) {
        // Validacija termina: mora biti na pun sat ili pola sata
        if (!isValidAppointmentTime(appointment.getStartTime())) {
            throw new IllegalArgumentException("Appointment must be on the hour or half-hour.");
        }
        // Validacija dužine termina
        if (!isValidAppointmentDuration(appointment.getStartTime(), appointment.getEndTime())) {
            throw new IllegalArgumentException("Appointment duration must be either 30 or 60 minutes.");
        }
        // Validacija preklapanja termina
        if (isOverlappingAppointment(appointment)) {
            throw new IllegalArgumentException("Appointment time overlaps with an existing appointment.");
        }
        return appointmentRepository.save(appointment);
    }






    public boolean isValidAppointmentTime(LocalDateTime startTime) {
        LocalTime truncated = startTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        return startTime.toLocalTime().equals(truncated.withMinute(0)) || startTime.toLocalTime().equals(truncated.withMinute(30));
    }

    public boolean isValidAppointmentDuration(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.equals(Duration.ofMinutes(30)) || duration.equals(Duration.ofMinutes(60));
    }

    public boolean canCancelAppointment(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        return appointment.getStartTime().isAfter(now.plusHours(cancellationDeadlineHours));
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public void cancelAppointment(Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            if (appointment.get().getStartTime().isBefore(now.plusHours(cancellationDeadlineHours))) {
                throw new IllegalArgumentException("Cannot cancel appointment within 24 hours of start time.");
            }
            appointmentRepository.deleteById(appointmentId);
        } else {
            throw new IllegalArgumentException("Appointment not found.");
        }
    }
}
