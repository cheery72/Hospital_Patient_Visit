package com.hdjunction.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdjunction.task.domain.Hospital;
import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.dto.CreatePatientRequest;
import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse.VisitDetailsDTO;
import com.hdjunction.task.dto.UpdatePatientRequest;
import com.hdjunction.task.service.PatientService;
import com.hdjunction.task.service.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
class PatientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private PatientService patientService;

    @MockBean
    private VisitService visitService;

    private final String BASE_URL = "/api/v1/";

    private final Long hospitalId = 1L;
    private final Long patientId = 1L;

    @BeforeEach()
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(
                                modifyUris().scheme("https").host("docs.api.com").removePort(), prettyPrint())
                        .withResponseDefaults(prettyPrint())
                )
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

        when(patientService.createPatient(any())).thenReturn(Patient.of());
        doNothing().when(visitService).createPatientVisit(any(),any());

        mockMvc.perform(post(BASE_URL+"{hospitalId}"+"/patients",hospitalId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("create-patient",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        requestFields(
                                fieldWithPath("patientName").description("환자 이름"),
                                fieldWithPath("genderCode").description("환자 성별코드"),
                                fieldWithPath("birthDate").description("환자 생년월일"),
                                fieldWithPath("phoneNumber").description("환자 핸드폰 번호")
                        )
                ));
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

        mockMvc.perform(patch(BASE_URL+"{patientId}"+"/patients",patientId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("update-patient-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        requestFields(
                                fieldWithPath("patientName").description("환자 이름"),
                                fieldWithPath("genderCode").description("환자 성별코드"),
                                fieldWithPath("birthDate").description("환자 생년월일"),
                                fieldWithPath("phoneNumber").description("환자 핸드폰 번호")
                        )
                ));
    }

    @Test
    @DisplayName("환자 삭제 테스트")
    public void patientSetDeleted() throws Exception {

        doNothing().when(patientService).deletePatient(any());

        mockMvc.perform(delete(BASE_URL+"{patientId}"+"/patients",patientId))
                .andExpect(status().isNoContent())
                .andDo(document("patient-set-deleted",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        )
                ));

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
                                .build()
                ))
                .build();

        // when
        when(patientService.findPatientWithVisits(patientId)).thenReturn(response);

        // then
        mockMvc.perform(get(BASE_URL+"/{patientId}/patients", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(patientId))
                .andExpect(jsonPath("$.patientName").value(patientName))
                .andExpect(jsonPath("$.patientRegistrationNumber").value(patientRegistrationNumber))
                .andExpect(jsonPath("$.patientGenderCode").value(patientGenderCode))
                .andExpect(jsonPath("$.patientBirthDate").value(patientBirthDate))
                .andExpect(jsonPath("$.patientPhoneNumber").value(patientPhoneNumber))
                .andExpect(jsonPath("$.visitDetails[0].visitId").value(visitId))
                .andExpect(jsonPath("$.visitDetails[0].visitHospitalName").value(hospital.getName()))
                .andExpect(jsonPath("$.visitDetails[0].visitRegistrationDateTime").value(visitRegistrationDateTime.toString()))
                .andDo(print())
                .andDo(document("find-patient-with-visits",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        responseFields(
                                fieldWithPath("patientId").description("환자 ID"),
                                fieldWithPath("patientName").description("환자명"),
                                fieldWithPath("patientRegistrationNumber").description("환자 등록번호"),
                                fieldWithPath("patientGenderCode").description("환자 성별코드"),
                                fieldWithPath("patientBirthDate").description("환자 생년월일"),
                                fieldWithPath("patientPhoneNumber").description("환자 핸드폰번호"),
                                fieldWithPath("visitDetails[].visitId").description("환자 방문 ID"),
                                fieldWithPath("visitDetails[].visitHospitalName").description("환자 방문 병원 이름"),
                                fieldWithPath("visitDetails[].visitRegistrationDateTime").description("환자 방문 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("환자 전체 목록 조회 테스트")
    public void findPatientDetailsTest() throws Exception {
        // given
        String searchParams = "name";
        String content = "이름";
        Pageable pageable = PageRequest.of(0, 2);

        String patientName = "환자";
        String patientRegistrationNumber = null;
        String patientBirthDate = null;

        Long patientId = 1L;
        String patientGenderCode = "M";
        String patientPhoneNumber = "123-456-7890";
        LocalDateTime visitDateTime = LocalDateTime.now();

        String formattedVisitDate = visitDateTime.toLocalDate().toString();
        PatientDetailsResponse patientDetailsResponse = PatientDetailsResponse.builder()
                .patientId(patientId)
                .patientName(patientName)
                .patientRegistrationNumber(patientRegistrationNumber)
                .patientGenderCode(patientGenderCode)
                .patientBirthDate(patientBirthDate)
                .patientPhoneNumber(patientPhoneNumber)
                .recentVisit(formattedVisitDate)
                .build();

        List<PatientDetailsResponse> list = new ArrayList<>();
        list.add(patientDetailsResponse);
        list.add(patientDetailsResponse);

        Page<PatientDetailsResponse> expectedPage = new PageImpl<>(list, pageable, list.size());

        // when
        when(patientService.findPatientDetails(searchParams, content, pageable)).thenReturn(expectedPage);

        // then
        mockMvc.perform(get(BASE_URL+"/{searchParams}/patients/search", searchParams)
                        .param("content", content)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].patientName").value(patientName))
                .andExpect(jsonPath("$.content[0].patientPhoneNumber").value(patientPhoneNumber))
                .andExpect(jsonPath("$.content[0].recentVisit").value(String.valueOf(visitDateTime.toLocalDate())))
                .andDo(document("find-patient-details",
                        pathParameters(
                                parameterWithName("searchParams").description("검색 유형")
                        ),
                        requestParameters(
                                parameterWithName("content").description("검색 내용"),
                                parameterWithName("page").description("요청할 페이지 번호"),
                                parameterWithName("size").description("한 페이지 요청할 개수")
                        ),
                        responseFields(
                                fieldWithPath("totalElements").description("요청 페이지까지의 데이터 개수"),
                                fieldWithPath("totalPages").description("요청 페이지 번호"),
                                fieldWithPath("size").description("요청 데이터 개수"),
                                fieldWithPath("number").description("요청 데이터 개수"),
                                fieldWithPath("content[].patientId").description("환자 ID"),
                                fieldWithPath("content[].patientName").description("환자 이름"),
                                fieldWithPath("content[].patientRegistrationNumber").description("환자 등록번호"),
                                fieldWithPath("content[].patientGenderCode").description("환자 성별 코드"),
                                fieldWithPath("content[].patientBirthDate").description("환자 생년월일"),
                                fieldWithPath("content[].patientPhoneNumber").description("환자 핸드폰번호"),
                                fieldWithPath("content[].recentVisit").description("환자 최근 병원 방문 날짜"),
                                fieldWithPath("pageable.sort.empty").ignored(),
                                fieldWithPath("pageable.sort.sorted").ignored(),
                                fieldWithPath("pageable.sort.unsorted").ignored(),
                                fieldWithPath("pageable.offset").ignored(),
                                fieldWithPath("pageable.pageNumber").description("요청 페이지 번호"),
                                fieldWithPath("pageable.pageSize").description("요청 데이터 개수"),
                                fieldWithPath("pageable.paged").ignored(),
                                fieldWithPath("pageable.unpaged").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("sort.empty").ignored(),
                                fieldWithPath("sort.sorted").ignored(),
                                fieldWithPath("sort.unsorted").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("empty").ignored()
                        )
                ));
    }

}
