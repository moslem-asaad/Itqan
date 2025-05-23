package com.example.itqan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemExistsException extends RuntimeException {
    public ItemExistsException(String message) {
        super(message);
    }
}