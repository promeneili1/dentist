package com.example.dentistappointment.repository;

import com.example.dentistappointment.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    Dentist findByJmbgAndPassword(String jmbg, String password);
}