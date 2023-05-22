package com.hdjunction.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreatePatientRequest {

    @NotNull
    private final Long hospitalId;

    @NotBlank
    private final String patientName;

    @NotBlank
    private final String genderCode;

    private final String birthDate;

    private final String phoneNumber;

}
