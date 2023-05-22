package com.hdjunction.task.dto;

public class CreatePatientRequest {

    private final Long hospitalId;

    private final String patientName;

    private final String genderCode;

    private final String birthDate;

    private final String phoneNumber;

    public CreatePatientRequest(Long hospitalId, String patientName, String genderCode, String birthDate, String phoneNumber) {
        this.hospitalId = hospitalId;
        this.patientName = patientName;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
