package com.hdjunction.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

}