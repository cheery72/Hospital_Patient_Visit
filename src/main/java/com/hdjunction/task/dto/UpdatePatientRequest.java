package com.hdjunction.task.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UpdatePatientRequest {

    @NotBlank
    private final String patientName;

    @NotBlank
    private final String genderCode;

    @NotBlank
    private final String birthDate;

    @NotBlank
    private final String phoneNumber;

}
