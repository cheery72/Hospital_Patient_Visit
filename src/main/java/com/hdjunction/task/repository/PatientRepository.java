package com.hdjunction.task.repository;

import com.hdjunction.task.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByRegistrationNumber(String registrationNumber);

}
