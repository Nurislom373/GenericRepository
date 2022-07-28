package org.khasanof.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlFieldEnums {
    VARCHAR("varchar"),
    INTEGER("integer"),
    BIGINT("bigint"),
    TIMESTAMP("timestamp"),
    DATE("date"),
    BOOLEAN("boolean"),
    SMALLINT("smallint"),
    FLOAT("float"),
    TIME("time"),
    SERIAL("serial"),
    NUMERIC("numeric");
    private final String value;
}
