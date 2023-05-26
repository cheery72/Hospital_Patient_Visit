package com.hdjunction.task.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PatientDetailsResponse {

    private final Long patientId;

    private final String patientName;

    private final String patientRegistrationNumber;

    private final String patientGenderCode;

    private final String patientBirthDate;

    private final String patientPhoneNumber;

    private final String recentVisit;

    @QueryProjection
    @Builder
    public PatientDetailsResponse(Long patientId, String patientName, String patientRegistrationNumber, String patientGenderCode, String patientBirthDate, String patientPhoneNumber, String recentVisit) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientRegistrationNumber = patientRegistrationNumber;
        this.patientGenderCode = patientGenderCode;
        this.patientBirthDate = patientBirthDate;
        this.patientPhoneNumber = patientPhoneNumber;
        this.recentVisit = recentVisit;
    }
}
