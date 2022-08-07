package org.khasanof.utils;

import org.khasanof.exception.exceptions.IdNotFoundException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public abstract class BaseUtils {
    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

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

    public static void classIdPresent(Field[] fields) {
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) {
                return;
            }
        }
        throw new IdNotFoundException("Class id field not found");
    }

    public static boolean isNumber(String type) {
        return type.contains("Integer")
                || type.contains("Float")
                || type.contains("BigDecimal")
                || type.contains("BigInteger")
                || type.contains("Long")
                || type.contains("Short")
                || type.contains("Byte")
                || type.contains("Double")
                || type.equals("int")
                || type.equals("byte")
                || type.equals("short")
                || type.equals("double")
                || type.equals("long")
                || type.equals("float");
    }
}
