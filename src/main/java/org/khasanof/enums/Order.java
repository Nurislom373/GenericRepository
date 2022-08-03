package org.khasanof.enums;

public enum Order {
    ASC("ASC"),
    DESC("DESC");

    Order(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private final String value;
}
