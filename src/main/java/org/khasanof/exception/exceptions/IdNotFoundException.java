package org.khasanof.exception.exceptions;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException() {
        super();
    }

    public IdNotFoundException(String message) {
        super(message);
    }
}
