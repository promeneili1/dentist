package com.example.dentistappointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dentists")
public class Dentist {

    @Id
    @Column(nullable = false)
    private String jmbg;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;



}
