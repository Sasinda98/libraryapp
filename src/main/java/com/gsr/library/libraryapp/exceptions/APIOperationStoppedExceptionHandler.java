package com.gsr.library.libraryapp.exceptions;

import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class APIOperationStoppedExceptionHandler {
    @ExceptionHandler(value = {OperationStoppedException.class})
    public ResponseEntity<Object> handleAPIException(OperationStoppedException ex){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(),
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }
}
