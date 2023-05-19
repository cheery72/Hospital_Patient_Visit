package com.hdjunction.task.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_visit")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_visit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @CreatedDate
    private LocalDateTime registrationDateTime;

    @Column(name = "visit_status_code", length = 10, nullable = false)
    private String statusCode;

    public Visit(Long id, Hospital hospital, Patient patient, LocalDateTime registrationDateTime, String statusCode) {
        this.id = id;
        this.hospital = hospital;
        this.patient = patient;
        this.registrationDateTime = registrationDateTime;
        this.statusCode = statusCode;
    }

    protected Visit() {

    }

    public Long getId() {
        return id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getRegistrationDateTime() {
        return registrationDateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
