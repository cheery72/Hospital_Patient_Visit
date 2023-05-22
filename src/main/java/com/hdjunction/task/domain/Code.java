package com.hdjunction.task.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@IdClass(CodeId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id
    @ManyToOne
    @JoinColumn(name = "code_group")
    private CodeGroup codeGroup;

    @Id
    @Column(length = 10, nullable = false)
    private String code;

    @Column(name = "code_name", length = 10, nullable = false)
    private String name;

}
