package org.khasanof.enums;

public enum SchemaEnum {
    NO_ANNOTATION("no_annotation"),
    ONLY_SCHEMA("only_schema"),
    ONLY_NAME("only_name"),
    TWO_VALUE("two_value");

    SchemaEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private final String value;
}
