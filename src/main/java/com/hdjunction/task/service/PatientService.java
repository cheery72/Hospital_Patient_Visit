package com.hdjunction.task.service;

import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.PatientRepository;
import com.hdjunction.task.common.DomainType;
import com.hdjunction.task.common.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    public PatientService(PatientRepository patientRepository, HospitalRepository hospitalRepository) {
        this.patientRepository = patientRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional
    public void createPatient(CreatePatientRequest createPatientRequest){
        Hospital hospital = hospitalRepository.findById(createPatientRequest.getHospitalId())
                .orElseThrow(NoSuchElementException::new);

        String randomString = UuidGenerator.generateUuid(DomainType.P);

        Optional<Patient> byRegistrationNumber = patientRepository.findByRegistrationNumber(randomString);

        if(byRegistrationNumber.isPresent()){
            randomString = UuidGenerator.generateUuid(DomainType.P);
            byRegistrationNumber = patientRepository.findByRegistrationNumber(randomString);

            if(byRegistrationNumber.isPresent()){
                throw new RuntimeException();
            }
        }

        Patient patient = Patient.build(createPatientRequest,hospital,randomString);

        patientRepository.save(patient);
    }
}
