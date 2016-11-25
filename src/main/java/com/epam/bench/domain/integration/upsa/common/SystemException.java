package com.epam.bench.domain.integration.upsa.common;

/**
 * Created by Tetiana_Antonenko1
 */
public class SystemException extends RuntimeException {

    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
