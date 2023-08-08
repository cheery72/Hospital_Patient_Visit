package com.hdjunction.task.service;

import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.Visit;
import com.hdjunction.task.exception.ClientException;
import com.hdjunction.task.exception.ErrorCode;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitService {

    private final HospitalRepository hospitalRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public void createPatientVisit(Long hospitalId, Patient patient) {
        Hospital hospital = findHospitalById(hospitalId);

        Visit visit = Visit.fromVisit(hospital, patient);

        visitRepository.save(visit);
    }

    private Hospital findHospitalById(Long hospitalId){
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND_HOSPITAL));
    }

}
