package com.gsr.library.libraryapp.exceptions.handlers;

import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class APINoResourceFoundException {
    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<Object> handleAPIException(NoResourceFoundException ex){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(),
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }
}
