
package com.lagnashree.customermanagement.exception;
import org.apache.coyote.BadRequestException;

public class InvalidInputException extends BadRequestException {
    public InvalidInputException(String message) {
        super(message);
    }
}

