package com.hdjunction.task.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hospital {

    @Id
    @Column(name = "hospital_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_name", length = 45, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String sanatoriumNumber;

    @Column(name = "hospital_director", length = 10, nullable = false)
    private String director;

    @OneToMany(mappedBy = "hospital")
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    private List<Visit> visits = new ArrayList<>();

    public Hospital(Long id, String name, String sanatoriumNumber, String director, List<Patient> patients, List<Visit> visits) {
        this.id = id;
        this.name = name;
        this.sanatoriumNumber = sanatoriumNumber;
        this.director = director;
        this.patients = patients;
        this.visits = visits;
    }

    protected Hospital() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSanatoriumNumber() {
        return sanatoriumNumber;
    }

    public String getDirector() {
        return director;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Visit> getVisits() {
        return visits;
    }
}
