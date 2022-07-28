package org.khasanof.utils;

import java.util.List;
import java.util.Objects;

public class BaseUtils {

    public static <T> void checkNotNullEntity(T entity) {
        if (Objects.isNull(entity)) {
            throw new RuntimeException("Entity is null!");
        }
    }

    public static <ID> void checkNotNullId(ID id) {
        if (Objects.isNull(id)) {
            throw new RuntimeException("Id is null!");
        }
    }

    public static <T> void checkNotNullList(List<T> tList) {
        if (tList.isEmpty()) {
            throw new RuntimeException("List Entity is null!");
        }
    }

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
