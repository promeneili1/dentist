package com.example.dentistappointment.service;


import com.example.dentistappointment.model.Patient;
import com.example.dentistappointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> findPatientByJMBG(String jmbg) {
        return patientRepository.findByJMBG(jmbg);
    }
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}