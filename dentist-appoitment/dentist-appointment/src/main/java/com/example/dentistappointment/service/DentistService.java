package com.example.dentistappointment.service;

import com.example.dentistappointment.model.Dentist;
import com.example.dentistappointment.model.Patient;
import com.example.dentistappointment.repository.DentistRepository;
import com.example.dentistappointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DentistService(DentistRepository dentistRepository, PatientRepository patientRepository) {
        this.dentistRepository = dentistRepository;
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatientByJmbg(String jmbg) {
        return patientRepository.findByJMBG(jmbg);
    }

    public Dentist saveDentist(Dentist dentist) {
        return dentistRepository.save(dentist);
    }

    public List<Dentist> findAllDentists() {
        return dentistRepository.findAll();
    }

    public Optional<Dentist> findDentistByJmbg(String jmbg) {
        return dentistRepository.findById(jmbg);
    }



    public Dentist loginDentist(String jmbg, String password) {
        Dentist dentist = dentistRepository.findByJmbgAndPassword(jmbg, password);
        return dentist;
    }
}
