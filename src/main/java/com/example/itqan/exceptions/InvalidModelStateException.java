package com.example.itqan.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidModelStateException extends RuntimeException {
    public InvalidModelStateException(String message) {
        super(message);
    }
}

