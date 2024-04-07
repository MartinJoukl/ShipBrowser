package com.example.shipbrowser.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundById extends RuntimeException {
    public ObjectNotFoundById() {
        super();
    }

    public ObjectNotFoundById(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundById(String message) {
        super(message);
    }

    public ObjectNotFoundById(Throwable cause) {
        super(cause);
    }
}
