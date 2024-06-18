package com.example.dentistappointment.controller;

import com.example.dentistappointment.model.Appointment;
import com.example.dentistappointment.model.Dentist;
import com.example.dentistappointment.model.Patient;
import com.example.dentistappointment.service.AppointmentService;
import com.example.dentistappointment.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dentists")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class DentistController {

    private final DentistService dentistService;
    private final AppointmentService appointmentService;

    @Autowired
    public DentistController(DentistService dentistService, AppointmentService appointmentService) {
        this.dentistService = dentistService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<Dentist> registerDentist(@RequestBody Dentist dentist) {
        Dentist createdDentist = dentistService.saveDentist(dentist);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDentist);
    }

    @PostMapping("/login")
    public ResponseEntity<Dentist> login(@RequestBody Dentist dentist) {
        Dentist loggedDentist = dentistService.loginDentist(dentist.getJmbg(), dentist.getPassword());
        if (loggedDentist != null) {
            return ResponseEntity.ok(loggedDentist);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dentist>> getAllDentists() {
        List<Dentist> dentists = dentistService.findAllDentists();
        return ResponseEntity.ok(dentists);
    }
    @GetMapping("/{jmbg}")
    public ResponseEntity<Patient> getPatientByJmbg(@PathVariable String jmbg) {
        Optional<Patient> patientOptional = dentistService.getPatientByJmbg(jmbg);
        return patientOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patients/{jmbg}")
    public ResponseEntity<Patient> getPatientByJMBG(@PathVariable String jmbg) {
        Optional<Patient> patientOptional = dentistService.getPatientByJmbg(jmbg);
        return patientOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
