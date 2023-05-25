package com.hdjunction.task.repository;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.repository.querydsl.PatientRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    Optional<Patient> findByRegistrationNumberAndDeletedFalse(String registrationNumber);
    Optional<Patient> findByIdAndDeletedFalse(Long patientId);

}
