package com.hdjunction.task.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient {

    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "patient_name", length = 45, nullable = false)
    private String name;

    @Column(name = "patient_registration_number", length = 13, nullable = false)
    private String registrationNumber;

    @Column(length = 10, nullable = false)
    private String genderCode;

    @Column(length = 10)
    private String birthDate;

    @Column(length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits = new ArrayList<>();

    public Patient(Long id, Hospital hospital, String name, String registrationNumber, String genderCode, String birthDate, String phoneNumber, List<Visit> visits) {
        this.id = id;
        this.hospital = hospital;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.visits = visits;
    }

    protected Patient() {

    }

    public Long getId() {
        return id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Visit> getVisits() {
        return visits;
    }
}
