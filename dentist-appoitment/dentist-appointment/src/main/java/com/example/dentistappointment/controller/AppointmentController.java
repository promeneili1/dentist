package com.example.dentistappointment.controller;

import com.example.dentistappointment.model.Appointment;
import com.example.dentistappointment.model.Patient;
import com.example.dentistappointment.service.AppointmentService;
import com.example.dentistappointment.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DentistService dentistService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, DentistService dentistService) {
        this.appointmentService = appointmentService;
        this.dentistService = dentistService;
    }



    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> body) {
        String jmbg = body.get("jmbg");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patient/{jmbg}")
    public ResponseEntity<List<Appointment>> getAppointmentsByJMBG(@PathVariable String jmbg) {
        List<Appointment> appointments = appointmentService.getAppointmentsByJMBG(jmbg);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/daily-appointments/{date}")
    public ResponseEntity<List<Appointment>> getDailyAppointments(@PathVariable LocalDate date) {
        List<Appointment> appointments = appointmentService.findAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/dentist")
    public ResponseEntity<?> createAppointmentForDentist(@RequestBody Appointment appointment) {
        try {
            Appointment createdAppointment = appointmentService.createAppointmentForDentist(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/by-jmbg")
    public ResponseEntity<Appointment> createAppointmentByJMBG(@RequestParam String jmbg, @RequestBody Appointment appointment) {
        Optional<Patient> patientOptional = dentistService.getPatientByJmbg(jmbg);
        if (patientOptional.isPresent()) {
            appointment.setPatientJMBG(jmbg);
            Appointment createdAppointment = dentistService.saveAppointment(appointment);
            return ResponseEntity.ok(createdAppointment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @GetMapping("/weekly-appointments")
    public ResponseEntity<List<Appointment>> getWeeklyAppointments(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Appointment> appointments = appointmentService.findAppointmentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointmentForPatient(@RequestBody Appointment appointment) {
        try {
            Appointment createdAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(createdAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }






    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment createdAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(createdAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
