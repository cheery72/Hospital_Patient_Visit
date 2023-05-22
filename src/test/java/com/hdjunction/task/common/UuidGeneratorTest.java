package com.hdjunction.task.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UuidGeneratorTest {

    @Test
    @DisplayName("유니크 키 테스트")
    public void test() {
        // given, when
        String registrationNumber = UuidGenerator.generateUuid(DomainType.P);

        // then
        assertThat(registrationNumber.length()).isEqualTo(13);
        assertThat(registrationNumber.substring(0,1)).isEqualTo("P");
    }

}