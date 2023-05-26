package com.hdjunction.task.service;

import com.hdjunction.task.common.DomainType;
import com.hdjunction.task.common.UuidGenerator;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.UpdatePatientRequest;
import com.hdjunction.task.exception.ClientException;
import com.hdjunction.task.exception.ErrorCode;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void updatePatient(Long patientId, UpdatePatientRequest updatePatientRequest) {
        Patient patient = findPatientId(patientId);

        patient.setPatientInfo(updatePatientRequest);
    }

    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = findPatientId(patientId);

        patient.setDeleted();
    }

    public PatientWithVisitsResponse findPatientWithVisits(Long patientId) {
        return patientRepository.findByIdWithVisits(patientId);
    }

    public Page<PatientDetailsResponse> findPatientDetails(String searchParams,String content,Pageable pageable) {
        return patientRepository.findByPatientDetails(searchParams,content, pageable);
    }

    private Patient findPatientId(Long patientId){
        return patientRepository.findByIdAndDeletedFalse(patientId)
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND_PATIENT));
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
        return patientRepository.findByRegistrationNumberAndDeletedFalse(randomString);
    }

    private boolean isPatientExistByRegistrationNumber(String registrationNumber) {
        return findPatientByRegistrationNumber(registrationNumber).isPresent();
    }

}
