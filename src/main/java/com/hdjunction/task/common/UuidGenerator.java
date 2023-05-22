package com.hdjunction.task.common;

import java.util.UUID;

public class UuidGenerator {

    public static String generateUuid(DomainType domainType) {
        return String.format("%s_%s", domainType.name(), UUID.randomUUID()).substring(0,13);
    }

}
