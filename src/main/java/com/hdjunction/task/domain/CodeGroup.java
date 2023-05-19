package com.hdjunction.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CodeGroup {

    @Id
    @Column(name = "code_group", length = 10, nullable = false)
    private String codeGroup;

    @Column(name = "code_group_name", length = 10, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String description;

    public CodeGroup(String codeGroup, String name, String description) {
        this.codeGroup = codeGroup;
        this.name = name;
        this.description = description;
    }

    public CodeGroup() {

    }

    public String getCodeGroup() {
        return codeGroup;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
