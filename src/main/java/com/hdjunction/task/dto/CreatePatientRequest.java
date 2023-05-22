package com.hdjunction.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CreatePatientRequest {

    @NotBlank
    private final String patientName;

    @NotBlank
    private final String genderCode;

    private final String birthDate;

    private final String phoneNumber;

}
