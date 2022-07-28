package org.khasanof.service;

public class BaseUtils {

    public static boolean isNumber(String type) {
        return type.contains("Integer")
                || type.contains("Float")
                || type.contains("BigDecimal")
                || type.contains("Short")
                || type.contains("Byte")
                || type.contains("Double")
                || type.equals("int")
                || type.equals("byte")
                || type.equals("short")
                || type.equals("double")
                || type.equals("lon")
                || type.equals("float");
    }
}
