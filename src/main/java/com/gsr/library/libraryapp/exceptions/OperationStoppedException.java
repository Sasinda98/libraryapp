package com.gsr.library.libraryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OperationStoppedException extends RuntimeException {
    public OperationStoppedException(String message) {
        super(message);
    }
}
