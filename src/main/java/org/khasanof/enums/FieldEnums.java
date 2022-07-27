package org.khasanof.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FieldEnums {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    DATE("Date"),
    LOCAL_DATE_TIME("LocalDateTime"),
    UUID("UUID"),
    FLOAT("Float");
    private final String value;
}
