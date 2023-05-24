package com.hdjunction.task.service;

import com.hdjunction.task.common.DomainType;
import com.hdjunction.task.common.UuidGenerator;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.exception.ClientException;
import com.hdjunction.task.exception.ErrorCode;
import com.hdjunction.task.repository.HospitalRepository;
import com.hdjunction.task.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private final CreatePatientRequest createPatientRequest = new CreatePatientRequest(
            "환자1",
            "Man1",
            "000101",
            "010-0000-0000");

    @Test
    @DisplayName("환자 생성 성공 테스트")
    public void createPatientTest(){
        // given
        Long hospitalId = 1L;

        Hospital hospital = Hospital.of();
        Patient patient = Patient.of();

        // when
        when(hospitalRepository.findById(any())).thenReturn(Optional.of(hospital));
        when(patientRepository.findByRegistrationNumber(anyString())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        patientService.createPatient(hospitalId,createPatientRequest);

        // then
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository).save(patientCaptor.capture());
        Patient savedPatient = patientCaptor.getValue();
        assertTrue(savedPatient.getRegistrationNumber().startsWith("P_"));
        verify(hospitalRepository).findById(hospitalId);
        verify(patientRepository).findByRegistrationNumber(anyString());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    @DisplayName("요청 받은 병원을 찾을 수 없을 경우 exception 테스트")
    public void createPatientNotFoundHospital_ExceptionTest() {
        // given
        Long hospitalId = 1L;

        // when
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

        ClientException exception = Assertions.assertThrows(ClientException.class,
                () -> patientService.createPatient(hospitalId, createPatientRequest));

        // then
        assertEquals(ErrorCode.NOT_FOUND_HOSPITAL, exception.getErrorCode());
        verify(hospitalRepository, Mockito.times(1)).findById(hospitalId);
    }

    @Test
    @DisplayName("generateUniqueRegistrationNumber 메서드 성공 테스트")
    public void generateUniqueRegistrationNumber_SuccessTest() {
        // given
        String registrationNumber = UuidGenerator.generateUuid(DomainType.P);

        // when
        String uniqueRegistrationNumber = ReflectionTestUtils.invokeMethod(patientService, "generateUniqueRegistrationNumber");

        // then
        assertNotNull(uniqueRegistrationNumber);
        assertTrue(uniqueRegistrationNumber.startsWith("P_"));
        assertNotEquals(registrationNumber, uniqueRegistrationNumber);
    }

}
