package org.khasanof.utils;

public abstract class Asserts {

    public Asserts() {
    }

    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
