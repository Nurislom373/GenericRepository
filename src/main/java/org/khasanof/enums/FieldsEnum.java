package org.khasanof.enums;


public enum FieldsEnum {
    BOOLEAN("Boolean", "BOOLEAN"),
    INT2("Short", "INT2"),
    INT4("Integer", "INT4"),
    INT8("Long", "INT8"),
    FLOAT4("Float", "FLOAT4"),
    FLOAT8("Double", "FLOAT8"),
    CHAR("Character", "CHAR"),
    VARCHAR("String", "VARCHAR"),
    TEXT("String", "TEXT"),
    ENUM("String", "ENUM"),
    NAME("String", "NAME"),
    SERIAL2("Short", "SERIAL2"),
    SERIAL4("Integer", "SERIAL4"),
    SERIAL8("Long", "SERIAL8"),
    NUMERIC("Long", "NUMERIC"),
    UUID("UUID", "UUID"),
    DATE("LocalDate", "DATE"),
    TIME("LocalTime", "TIME"),
    TIMETZ("OffsetTime", "TIMETZ"),
    TIMESTAMP("LocalDateTime", "TIMESTAMP"),
    TIMESTAMPTZ("OffsetDateTime", "TIMESTAMPTZ"),
    INTERVAL("Interval", "INTERVAL"),
    BYTEA("Buffer", "BYTEA"),
    JSON("Json", "JSON"),
    JSONB("Json", "JSONB"),
    POINT("Point", "POINT"),
    LINE("Line", "LINE"),
    LSEG("LineSegment", "LSEG"),
    BOX("Box", "BOX"),
    PATH("Path", "PATH"),
    POLYGON("Polygon", "POLYGON"),
    CIRCLE("Circle", "CIRCLE"),
    UNKNOWN("String", "VARCHAR");

    FieldsEnum(String java, String postgres) {
        this.java = java;
        this.postgres = postgres;
    }

    private final String java;
    private final String postgres;
}
