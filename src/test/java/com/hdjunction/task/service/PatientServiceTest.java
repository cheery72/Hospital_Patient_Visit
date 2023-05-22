package com.hdjunction.task.service;

import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    @DisplayName("환자 생성 성공 테스트")
    public void createPatientTest(){
        // given
        CreatePatientRequest createPatientRequest = new CreatePatientRequest(
                1L,
                "환자1",
                "Man1",
                "000101",
                "010-0000-0000");

        Hospital hospital = Hospital.of();

        // when
        when(hospitalRepository.findById(any())).thenReturn(Optional.of(hospital));
        when(patientRepository.save(any(Patient.class))).thenReturn(Patient.of());

        patientService.createPatient(createPatientRequest);

        // then
        verify(hospitalRepository).findById(createPatientRequest.getHospitalId());
        verify(patientRepository).save(any(Patient.class));
    }


}
