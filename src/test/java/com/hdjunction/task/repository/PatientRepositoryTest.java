package com.hdjunction.task.repository;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.Visit;
import com.hdjunction.task.dto.PatientDetailsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("환자 전체 목록 테스트")
    public void findByPatientDetailsTest() {
        // given
        String searchParams = "name";
        String content = "이름";
        Pageable pageable = PageRequest.of(0, 2);

        List<Visit> visits = new ArrayList<>();

        Visit visit = Visit.builder()
                .registrationDateTime(LocalDateTime.now())
                .build();

        visits.add(visit);
        visits.add(visit);
        visits.add(visit);

        for (int i = 0; i < 3; i++) {
            Patient patient = Patient.builder()
                    .name("이름")
                    .genderCode("123")
                    .registrationNumber("12345678"+i)
                    .visits(visits)
                    .build();

            patientRepository.save(patient);
        }

        // when
        Page<PatientDetailsResponse> result = patientRepository.findByPatientDetails(searchParams, content, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).getPatientRegistrationNumber()).isEqualTo("123456780");
        assertThat(result.getContent().get(1).getPatientRegistrationNumber()).isEqualTo("123456781");
        assertThat(result.getContent().get(0).getPatientName()).isEqualTo("이름");
        assertThat(result.getPageable().getOffset()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(2);
    }
}
