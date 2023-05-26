package com.hdjunction.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse.VisitDetailsDTO;
import com.hdjunction.task.dto.UpdatePatientRequest;
import com.hdjunction.task.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private PatientService patientService;

    private final String BASE_URL = "/api/v1/";

    private final Long hospitalId = 1L;
    private final Long patientId = 1L;

    @BeforeEach()
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("환자 등록 테스트")
    public void createPatientTest() throws Exception {
        String body = objectMapper.writeValueAsString(
                new CreatePatientRequest(
                        "환자1",
                        "Man1",
                        "000101",
                        "010-0000-0000")
        );

        doNothing().when(patientService).createPatient(any(), any());

        mockMvc.perform(post(BASE_URL+hospitalId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("환자 등록 실패 테스트, patientName이 null일 경우")
    public void createPatientFailTestWhenPatientNameIsNull() throws Exception {
        String body = objectMapper.writeValueAsString(
                new CreatePatientRequest(
                        null,
                        "Man1",
                        "000101",
                        "010-0000-0000")
        );

        doNothing().when(patientService).createPatient(any(), any());

        mockMvc.perform(post(BASE_URL+hospitalId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("환자 등록 실패 테스트, genderCode가 null일 경우")
    public void createPatientFailTestWhenGenderCodeIsNull() throws Exception {
        String body = objectMapper.writeValueAsString(
                new CreatePatientRequest(
                        "환자1",
                        null,
                        "000101",
                        "010-0000-0000")
        );

        doNothing().when(patientService).createPatient(any(), any());

        mockMvc.perform(post(BASE_URL+hospitalId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("환자 수정 성공 테스트")
    public void updatePatientTest() throws Exception {
        String body = objectMapper.writeValueAsString(
                new UpdatePatientRequest(
                        "환자1",
                        "Man1",
                        "000101",
                        "010-0000-0000")
        );

        doNothing().when(patientService).updatePatient(any(), any());

        mockMvc.perform(patch(BASE_URL+patientId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("환자 수정 실패 테스트, UpdatePatientRequest가 empty일 경우")
    public void updatePatientFailTestWhenUpdatePatientRequestIsEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(
                new UpdatePatientRequest(
                        "",
                        "",
                        "",
                        "")
        );

        doNothing().when(patientService).updatePatient(any(), any());

        mockMvc.perform(patch(BASE_URL+patientId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("환자 수정 실패 테스트, UpdatePatientRequest가 null일 경우")
    public void updatePatientFailTestWhenUpdatePatientRequestIsNull() throws Exception {
        String body = objectMapper.writeValueAsString(
                new UpdatePatientRequest(
                        null,
                        null,
                        null,
                        null)
        );

        doNothing().when(patientService).updatePatient(any(), any());

        mockMvc.perform(patch(BASE_URL+patientId+"/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("환자 삭제 테스트")
    public void patientSetDeleted() throws Exception {

        doNothing().when(patientService).deletePatient(any());

        mockMvc.perform(delete(BASE_URL+patientId+"/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("환자 조회 테스트")
    public void findPatientWithVisits() throws Exception {

        // given
        Long patientId = 1L;
        String patientName = "환자";
        String patientRegistrationNumber = "123456789";
        String patientGenderCode = "M";
        String patientBirthDate = "1995-01-01";
        String patientPhoneNumber = "123-456-7890";

        Long visitId = 1L;
        LocalDateTime visitRegistrationDateTime = LocalDateTime.now();
        String visitStatusCode = "방문코드";

        Hospital hospital = Mockito.mock(Hospital.class);

        PatientWithVisitsResponse response = PatientWithVisitsResponse.builder()
                .patientId(patientId)
                .patientName(patientName)
                .patientRegistrationNumber(patientRegistrationNumber)
                .patientGenderCode(patientGenderCode)
                .patientBirthDate(patientBirthDate)
                .patientPhoneNumber(patientPhoneNumber)
                .visitDetails(List.of(
                        VisitDetailsDTO.builder()
                                .visitId(visitId)
                                .visitHospitalName(hospital.getName())
                                .visitRegistrationDateTime(visitRegistrationDateTime.toString())
                                .visitStatusCode(visitStatusCode)
                                .build()
                ))
                .build();

        // when
        when(patientService.findPatientWithVisits(patientId)).thenReturn(response);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/{patientId}/patients", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.patientId").value(patientId))
                .andExpect(jsonPath("$.patientName").value(patientName))
                .andExpect(jsonPath("$.patientRegistrationNumber").value(patientRegistrationNumber))
                .andExpect(jsonPath("$.patientGenderCode").value(patientGenderCode))
                .andExpect(jsonPath("$.patientBirthDate").value(patientBirthDate))
                .andExpect(jsonPath("$.patientPhoneNumber").value(patientPhoneNumber))
                .andExpect(jsonPath("$.visitDetails[0].visitId").value(visitId))
                .andExpect(jsonPath("$.visitDetails[0].visitHospitalName").value(hospital.getName()))
                .andExpect(jsonPath("$.visitDetails[0].visitRegistrationDateTime").value(visitRegistrationDateTime.toString()))
                .andExpect(jsonPath("$.visitDetails[0].visitStatusCode").value(visitStatusCode))
                .andDo(print());
    }
}