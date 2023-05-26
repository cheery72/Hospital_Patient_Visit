package com.hdjunction.task.repository.querydsl;

import com.hdjunction.task.dto.PatientWithVisitsResponse;

public interface PatientRepositoryCustom {

    PatientWithVisitsResponse findByIdWithVisits(Long patientId);

}
