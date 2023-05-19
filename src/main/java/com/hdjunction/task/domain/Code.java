package com.hdjunction.task.domain;

import javax.persistence.*;

@Entity
@IdClass(CodeId.class)
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

    public Code(CodeGroup codeGroup, String code, String name) {
        this.codeGroup = codeGroup;
        this.code = code;
        this.name = name;
    }

    public Code() {

    }

    public CodeGroup getCodeGroup() {
        return codeGroup;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
