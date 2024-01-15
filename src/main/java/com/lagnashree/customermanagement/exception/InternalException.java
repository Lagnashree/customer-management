package com.lagnashree.customermanagement.exception;

import org.apache.coyote.BadRequestException;

public class InternalException extends RuntimeException {
    public InternalException(String message) {
        super(message);
    }
}
