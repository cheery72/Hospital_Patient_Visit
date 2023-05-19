package com.hdjunction.task.domain;

import java.io.Serializable;
import java.util.Objects;

public class CodeId implements Serializable {
    private String codeGroup;
    private String code;

    public CodeId() {
    }

    public CodeId(String codeGroup, String code) {
        this.codeGroup = codeGroup;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeId codeId = (CodeId) o;
        return Objects.equals(codeGroup, codeId.codeGroup) && Objects.equals(code, codeId.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeGroup, code);
    }
}
