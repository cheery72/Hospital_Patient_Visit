package com.hdjunction.task.controller;

import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/{hospitalId}/patients")
    public ResponseEntity<Object> createPatient(
            @PathVariable Long hospitalId,
            @RequestBody @Valid CreatePatientRequest createPatientRequest) {

        patientService.createPatient(hospitalId, createPatientRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
