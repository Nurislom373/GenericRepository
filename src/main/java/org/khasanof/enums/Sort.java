package org.khasanof.enums;

public enum Sort {
    ASC("ASC"),
    DESC("DESC");

    Sort(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private final String value;
}
