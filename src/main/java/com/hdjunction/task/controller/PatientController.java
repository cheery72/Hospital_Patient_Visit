package com.hdjunction.task.controller;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.UpdatePatientRequest;
import com.hdjunction.task.service.PatientService;
import com.hdjunction.task.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final VisitService visitService;

    @PostMapping("/{hospitalId}/patients")
    public ResponseEntity<Object> createPatient(
            @PathVariable Long hospitalId,
            @RequestBody @Valid CreatePatientRequest createPatientRequest) {

        Patient patient = patientService.createPatient(createPatientRequest);

        visitService.createPatientVisit(hospitalId, patient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PatchMapping("/{patientId}/patients")
    public ResponseEntity<Object> updatePatient(
            @PathVariable Long patientId,
            @RequestBody @Valid UpdatePatientRequest updatePatientRequest) {

        patientService.updatePatient(patientId,updatePatientRequest);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{patientId}/patients")
    public ResponseEntity<Object> deletePatient(
            @PathVariable Long patientId) {

        patientService.deletePatient(patientId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{patientId}/patients")
    public ResponseEntity<PatientWithVisitsResponse> findPatientWithVisits(
            @PathVariable Long patientId) {

        return ResponseEntity
                .ok(patientService.findPatientWithVisits(patientId));
    }

    @GetMapping("/{searchParams}/patients/search")
    public ResponseEntity<Page<PatientDetailsResponse>> findPatientDetails(
            @PathVariable String searchParams,
            @RequestParam(required = false) String content,
            Pageable pageable) {

        return ResponseEntity
                .ok(patientService.findPatientDetails(searchParams,content, pageable));
    }
}