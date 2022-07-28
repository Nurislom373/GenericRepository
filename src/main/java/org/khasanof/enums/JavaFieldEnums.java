package org.khasanof.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JavaFieldEnums {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    DATE("Date"),
    BYTE("Byte"),
    TIMESTAMP("Timestamp"),
    TIME("Time"),
    SHORT("Short"),
    BIG_DECIMAL("BigDecimal"),
    BOOLEAN("Boolean"),
    LOCAL_DATE_TIME("LocalDateTime"),
    UUID("UUID"),
    FLOAT("Float");
    private final String value;
}
