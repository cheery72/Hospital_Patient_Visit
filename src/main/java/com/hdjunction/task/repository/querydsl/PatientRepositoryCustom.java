package com.hdjunction.task.repository.querydsl;

import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientRepositoryCustom {

    PatientWithVisitsResponse findByIdWithVisits(Long patientId);
    Page<PatientDetailsResponse> findByPatientDetails(String searchParams,String content,Pageable pageable);
}
