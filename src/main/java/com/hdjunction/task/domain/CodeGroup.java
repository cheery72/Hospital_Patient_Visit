package com.hdjunction.task.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeGroup {

    @Id
    @Column(name = "code_group", length = 10, nullable = false)
    private String codeGroup;

    @Column(name = "code_group_name", length = 10, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String description;

}
