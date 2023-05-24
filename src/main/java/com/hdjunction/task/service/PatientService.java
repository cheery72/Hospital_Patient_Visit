package com.hdjunction.task.service;

import com.hdjunction.task.common.DomainType;
import com.hdjunction.task.common.UuidGenerator;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.exception.ClientException;
import com.hdjunction.task.exception.ErrorCode;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    public void createPatient(Long hospitalId, CreatePatientRequest createPatientRequest) {
        Hospital hospital = findHospitalById(hospitalId);

        String registrationNumber = generateUniqueRegistrationNumber();

        Patient patient = Patient.of(createPatientRequest, hospital, registrationNumber);

        patientRepository.save(patient);
    }

    private String generateUniqueRegistrationNumber() {
        String registrationNumber;

        do {
            registrationNumber = UuidGenerator.generateUuid(DomainType.P);
        } while (isPatientExistByRegistrationNumber(registrationNumber));

        return registrationNumber;
    }

    private Hospital findHospitalById(Long hospitalId){
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND_HOSPITAL));
    }

    private Optional<Patient> findPatientByRegistrationNumber(String randomString){
        return patientRepository.findByRegistrationNumber(randomString);
    }

    private boolean isPatientExistByRegistrationNumber(String registrationNumber) {
        return findPatientByRegistrationNumber(registrationNumber).isPresent();
    }

}
