package com.gsr.library.libraryapp.exceptions.handlers;

import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class APIValidationExceptionHandler {
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handleAPIException(HttpServletRequest request, ValidationException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String servletPath = request.getServletPath();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(), servletPath,
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }
}
