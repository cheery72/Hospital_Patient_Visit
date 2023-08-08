package com.hdjunction.task.dto;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.Visit;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PatientWithVisitsResponse {

    private final Long patientId;

    private final String patientName;

    private final String patientRegistrationNumber;

    private final String patientGenderCode;

    private final String patientBirthDate;

    private final String patientPhoneNumber;

    private final List<VisitDetailsDTO> visitDetails;


    @QueryProjection
    @Builder
    public PatientWithVisitsResponse(Long patientId, String patientName, String patientRegistrationNumber, String patientGenderCode, String patientBirthDate, String patientPhoneNumber, List<VisitDetailsDTO> visitDetails) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientRegistrationNumber = patientRegistrationNumber;
        this.patientGenderCode = patientGenderCode;
        this.patientBirthDate = patientBirthDate;
        this.patientPhoneNumber = patientPhoneNumber;
        this.visitDetails = visitDetails;
    }


    public static PatientWithVisitsResponse fromPatientWithVisitsResponse(Patient patient) {
        return PatientWithVisitsResponse.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .patientRegistrationNumber(patient.getRegistrationNumber())
                .patientGenderCode(patient.getGenderCode())
                .patientBirthDate(patient.getBirthDate())
                .patientPhoneNumber(patient.getPhoneNumber())
                .visitDetails(VisitDetailsDTO.fromVisitDetails(patient.getVisits()))
                .build();
    }

    @Getter
    public static class VisitDetailsDTO {

        private final Long visitId;
        private final String visitHospitalName;
        private final String visitRegistrationDateTime;

        @Builder
        public VisitDetailsDTO(Long visitId, String visitHospitalName, String visitRegistrationDateTime) {
            this.visitId = visitId;
            this.visitHospitalName = visitHospitalName;
            this.visitRegistrationDateTime = visitRegistrationDateTime;
        }

        public static List<VisitDetailsDTO> fromVisitDetails(List<Visit> visits){
            return visits.stream()
                    .map(v -> (
                        VisitDetailsDTO.builder()
                            .visitId(v.getId())
                            .visitHospitalName(String.valueOf(v.getHospital()))
                            .visitRegistrationDateTime(String.valueOf(v.getRegistrationDateTime()))
                            .build()))
                    .collect(Collectors.toList());
        }

    }

}