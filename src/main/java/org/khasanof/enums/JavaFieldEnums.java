package org.khasanof.enums;

public enum JavaFieldEnums {
    BOOLEAN("Boolean"),
    STRING("String"),
    CHARACTER("Character"),
    LOCAL_DATE("LocalDate"),
    LOCAL_TIME("LocalTime"),
    OFFSET_TIME("OffsetTime"),
    LOCAL_DATE_TIME("LocalDateTime"),
    TIMESTAMP("Timestamp"),
    TIME("Time"),
    BYTE("Byte"),
    SHORT("Short"),
    INTEGER("Integer"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    DATE("Date"),
    BIG_DECIMAL("BigDecimal"),
    POINT("Point"),
    CIRCLE("Circle"),
    INTERVAL("Interval"),
    BYTEA("Buffer"),
    UUID("UUID");
    private final String value;

    JavaFieldEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
