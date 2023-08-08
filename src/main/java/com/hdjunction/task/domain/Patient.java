package com.hdjunction.task.domain;

import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.UpdatePatientRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_patient_name", columnList = "patient_name"),
        @Index(name = "idx_patient_registration_number", columnList = "patient_registration_number"),
        @Index(name = "idx_birth_date", columnList = "birth_date")
})
public class Patient extends BaseTime{

    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", length = 45, nullable = false)
    private String name;

    @Column(name = "patient_registration_number", length = 13, nullable = false)
    private String registrationNumber;

    @Column(length = 10, nullable = false)
    private String genderCode;

    @Column(name = "birth_date", length = 10)
    private String birthDate;

    @Column(length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits = new ArrayList<>();

    private boolean deleted = false;

    @Builder
    public Patient(Long id, String name, String registrationNumber, String genderCode, String birthDate, String phoneNumber, List<Visit> visits, boolean deleted) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.visits = visits;
        this.deleted = deleted;
    }

    public static Patient of(CreatePatientRequest createPatientRequest, String registrationNumber) {
        return Patient.builder()
                .name(createPatientRequest.getPatientName())
                .registrationNumber(registrationNumber)
                .genderCode(createPatientRequest.getGenderCode())
                .birthDate(createPatientRequest.getBirthDate())
                .phoneNumber(createPatientRequest.getPhoneNumber())
                .build();
    }

    public void setPatientInfo(UpdatePatientRequest updatePatientRequest) {
        this.name = updatePatientRequest.getPatientName();
        this.genderCode = updatePatientRequest.getGenderCode();
        this.birthDate = updatePatientRequest.getBirthDate();
        this.phoneNumber = updatePatientRequest.getPhoneNumber();
    }

    public void setDeleted(){
        this.deleted = true;
    }

    public static Patient of() {
        return new Patient();
    }
}
