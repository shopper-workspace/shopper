package com.shopper.shared.outbox;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException() {
        super();
    }

    public DuplicateEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEventException(String message) {
        super(message);
    }

    public DuplicateEventException(Throwable cause) {
        super(cause);
    }
}
