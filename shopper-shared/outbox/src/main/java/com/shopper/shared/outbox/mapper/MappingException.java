package com.shopper.shared.outbox.mapper;

public class MappingException extends RuntimeException {

    public MappingException(Throwable t) {
        super(t);
    }
}
