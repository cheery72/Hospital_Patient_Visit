package com.hdjunction.task.service;

import com.hdjunction.task.common.DomainType;
import com.hdjunction.task.common.UuidGenerator;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.Visit;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse.VisitDetailsDTO;
import com.hdjunction.task.dto.UpdatePatientRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    private final CreatePatientRequest createPatientRequest = new CreatePatientRequest(
            "환자1",
            "Man1",
            "000101",
            "010-0000-0000");

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("환자 생성 성공 테스트")
    public void createPatientTest(){
        // given
        Long hospitalId = 1L;

        Hospital hospital = Hospital.of();
        Patient patient = Patient.of();

        // when
        when(patientRepository.findByRegistrationNumberAndDeletedFalse(anyString())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        patientService.createPatient(createPatientRequest);


        // then
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository).save(patientCaptor.capture());
        Patient savedPatient = patientCaptor.getValue();
        assertTrue(savedPatient.getRegistrationNumber().startsWith("P_"));
        assertFalse(savedPatient.isDeleted());
        verify(patientRepository).findByRegistrationNumberAndDeletedFalse(anyString());
        verify(patientRepository).save(any(Patient.class));
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

    @Test
    @DisplayName("UpdatePatientRequest Validation Empty 테스트")
    public void updatePatientRequest_EmptyTest(){
        // given
        UpdatePatientRequest request = new UpdatePatientRequest(
                "",
                "",
                "",
                ""
        );

        // when
        Set<ConstraintViolation<UpdatePatientRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(4);

    }

    @Test
    @DisplayName("UpdatePatientRequest Validation Null 테스트")
    public void updatePatientRequest_NullTest(){
        // given
        UpdatePatientRequest request = new UpdatePatientRequest(
                null,
                null,
                null,
                null
        );

        // when
        Set<ConstraintViolation<UpdatePatientRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(4);
    }

    @Test
    @DisplayName("요청 받은 환자를 찾을 수 없을 경우 exception 테스트")
    public void findPatientNotFoundPatient_ExceptionTest() {
        // given
        Long patientId = 1L;

        // when
        when(patientRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.empty());
        ClientException exception = Assertions.assertThrows(ClientException.class,
                () -> patientService.updatePatient(patientId, Mockito.mock(UpdatePatientRequest.class)));

        // then
        assertEquals(ErrorCode.NOT_FOUND_PATIENT, exception.getErrorCode());
        verify(patientRepository, Mockito.times(1)).findByIdAndDeletedFalse(patientId);
    }

    @Test
    @DisplayName("환자 정보 수정 기능 테스트")
    public void updatePatientTest() {
        // given
        Long patientId = 1L;
        Patient patient = Patient.of();
        UpdatePatientRequest updatePatientRequest = new UpdatePatientRequest(
                "환자1",
                "Man1",
                "000101",
                "010-0000-0000");

        // then
        when(patientRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(patient));
        patientService.updatePatient(patientId,updatePatientRequest);

        assertEquals(updatePatientRequest.getPatientName(), patient.getName());
        assertEquals(updatePatientRequest.getBirthDate(), patient.getBirthDate());
        assertEquals(updatePatientRequest.getGenderCode(), patient.getGenderCode());
        assertEquals(updatePatientRequest.getPhoneNumber(), patient.getPhoneNumber());
        verify(patientRepository).findByIdAndDeletedFalse(patientId);
    }

    @Test
    @DisplayName("환자 데이터 삭제")
    public void patientSetDeleted() {
        // given
        Long patientId = 1L;
        Patient patient = Patient.of();

        // when
        when(patientRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(patient));
        patientService.deletePatient(patientId);

        assertTrue(patient.isDeleted());
        verify(patientRepository).findByIdAndDeletedFalse(patientId);
        verify(patientRepository, Mockito.times(1)).findByIdAndDeletedFalse(patientId);
    }


    @Test
    @DisplayName("환자 조회")
    public void findPatientWithVisitsTest() {
        // given
        Long patientId = 1L;
        String patientName = "환자";
        String patientRegistrationNumber = "123456789";
        String patientGenderCode = "M";
        String patientBirthDate = "1995-01-01";
        String patientPhoneNumber = "123-456-7890";

        Hospital hospital = Hospital.builder()
                .id(1L)
                .name("병원")
                .sanatoriumNumber("12345")
                .director("병원장")
                .build();

        Long visitId = 1L;
        LocalDateTime visitRegistrationDateTime = LocalDateTime.now();
        String visitStatusCode = "방문코드";

        Visit visit = Visit.builder()
                .id(visitId)
                .hospital(hospital)
                .registrationDateTime(visitRegistrationDateTime)
                .build();

        List<Visit> visits = new ArrayList<>();
        visits.add(visit);

        Patient patient = Patient.builder()
                .id(patientId)
                .name(patientName)
                .registrationNumber(patientRegistrationNumber)
                .genderCode(patientGenderCode)
                .birthDate(patientBirthDate)
                .phoneNumber(patientPhoneNumber)
                .visits(visits)
                .build();

        // when
        when(patientRepository.findByIdWithVisits(anyLong())).thenReturn(PatientWithVisitsResponse.fromPatientWithVisitsResponse(patient));

        PatientWithVisitsResponse response = patientService.findPatientWithVisits(patientId);

        // then
        Assertions.assertEquals(patientId, response.getPatientId());
        Assertions.assertEquals(patientName, response.getPatientName());
        Assertions.assertEquals(patientRegistrationNumber, response.getPatientRegistrationNumber());
        Assertions.assertEquals(patientGenderCode, response.getPatientGenderCode());
        Assertions.assertEquals(patientBirthDate, response.getPatientBirthDate());
        Assertions.assertEquals(patientPhoneNumber, response.getPatientPhoneNumber());
        Assertions.assertEquals(1, response.getVisitDetails().size());
        VisitDetailsDTO visitDetails = response.getVisitDetails().get(0);
        Assertions.assertEquals(visitId, visitDetails.getVisitId());
        Assertions.assertEquals(String.valueOf(visitRegistrationDateTime), visitDetails.getVisitRegistrationDateTime());

    }


    @Test
    @DisplayName("환자 목록 조회 테스트, patientName 선택할 경우")
    public void findPatientDetails_PatientName_Test() {

        // given
        String searchParams = "name";
        String patientName = "환자";
        Pageable pageable = PageRequest.of(0, 2);

        PatientDetailsResponse patientDetailsResponse = createPatientDetailsResponse();
        List<PatientDetailsResponse> patientDetailsResponses = createPatientDetailListResponse(patientDetailsResponse);


        // when
        when(patientRepository.findByPatientDetails(eq(searchParams),eq(patientName),any(Pageable.class)))
                .thenReturn(new PageImpl<>(patientDetailsResponses));

        Page<PatientDetailsResponse> result = patientService.findPatientDetails(searchParams,patientName,pageable);

        // then
        assertThat(result.getContent()).isEqualTo(patientDetailsResponses);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getPatientName()).isEqualTo(patientName);
        assertThat(result.getContent().get(0).getPatientGenderCode()).isEqualTo(patientDetailsResponse.getPatientGenderCode());
        assertThat(result.getContent().get(0).getPatientPhoneNumber()).isEqualTo(patientDetailsResponse.getPatientPhoneNumber());
        assertThat(result.getContent().get(0).getRecentVisit()).isEqualTo(String.valueOf(patientDetailsResponse.getRecentVisit()));
    }


    @Test
    @DisplayName("환자 목록 조회 테스트, patientRegistrationNumber 선택할 경우")
    public void findPatientDetails_PatientRegistrationNumber_Test() {
        // given
        String searchParams = "registrationNumber";
        String patientRegistrationNumber = "123456789";
        Pageable pageable = PageRequest.of(0, 2);

        PatientDetailsResponse patientDetailsResponse = createPatientDetailsResponse();
        List<PatientDetailsResponse> patientDetailsResponses = createPatientDetailListResponse(patientDetailsResponse);

        // when
        when(patientRepository.findByPatientDetails(eq(searchParams),eq(patientRegistrationNumber),any(Pageable.class)))
                .thenReturn(new PageImpl<>(patientDetailsResponses));

        Page<PatientDetailsResponse> result = patientService.findPatientDetails(searchParams,patientRegistrationNumber,pageable);


        // then
        assertThat(result.getContent()).isEqualTo(patientDetailsResponses);
        assertThat(result.getContent().get(0).getPatientRegistrationNumber()).isEqualTo(patientRegistrationNumber);
        assertThat(result.getContent().get(0).getPatientGenderCode()).isEqualTo(patientDetailsResponse.getPatientGenderCode());
        assertThat(result.getContent().get(0).getPatientPhoneNumber()).isEqualTo(patientDetailsResponse.getPatientPhoneNumber());
        assertThat(result.getContent().get(0).getRecentVisit()).isEqualTo(String.valueOf(patientDetailsResponse.getRecentVisit()));
    }

    @Test
    @DisplayName("환자 목록 조회 테스트, patientBirthDate 선택할 경우")
    public void findPatientDetails_PatientBirthDate_Test() {

        // given
        String searchParams = "birth";
        String patientBirthDate = "1995-01-01";
        Pageable pageable = PageRequest.of(0, 2);

        PatientDetailsResponse patientDetailsResponse = createPatientDetailsResponse();
        List<PatientDetailsResponse> patientDetailsResponses = createPatientDetailListResponse(patientDetailsResponse);

        // when
        when(patientRepository.findByPatientDetails(eq(searchParams),eq(patientBirthDate),any(Pageable.class)))
                .thenReturn(new PageImpl<>(patientDetailsResponses));

        Page<PatientDetailsResponse> result = patientService.findPatientDetails(searchParams,patientBirthDate,pageable);

        // then
        assertThat(result.getContent()).isEqualTo(patientDetailsResponses);
        assertThat(result.getContent().get(0).getPatientBirthDate()).isEqualTo(patientBirthDate);
        assertThat(result.getContent().get(0).getPatientGenderCode()).isEqualTo(patientDetailsResponse.getPatientGenderCode());
        assertThat(result.getContent().get(0).getPatientPhoneNumber()).isEqualTo(patientDetailsResponse.getPatientPhoneNumber());
        assertThat(result.getContent().get(0).getRecentVisit()).isEqualTo(String.valueOf(patientDetailsResponse.getRecentVisit()));
    }

    private List<PatientDetailsResponse> createPatientDetailListResponse(PatientDetailsResponse patientDetailsResponse){
        List<PatientDetailsResponse> patientDetailsResponses = new ArrayList<>();
        patientDetailsResponses.add(patientDetailsResponse);
        patientDetailsResponses.add(patientDetailsResponse);

        return patientDetailsResponses;
    }

    private PatientDetailsResponse createPatientDetailsResponse() {
        Long patientId = 1L;
        String patientName = "환자";
        String patientRegistrationNumber = "123456789";
        String patientGenderCode = "M";
        String patientBirthDate = "1995-01-01";
        String patientPhoneNumber = "123-456-7890";
        String visitDateTime = LocalDateTime.now().toLocalDate().toString();

        return PatientDetailsResponse.builder()
                .patientId(patientId)
                .patientName(patientName)
                .patientRegistrationNumber(patientRegistrationNumber)
                .patientGenderCode(patientGenderCode)
                .patientBirthDate(patientBirthDate)
                .patientPhoneNumber(patientPhoneNumber)
                .recentVisit(visitDateTime)
                .build();
    }
}

