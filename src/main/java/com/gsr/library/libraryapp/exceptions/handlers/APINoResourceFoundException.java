package com.gsr.library.libraryapp.exceptions.handlers;

import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class APINoResourceFoundException {
    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<Object> handleAPIException(HttpServletRequest request, NoResourceFoundException ex){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String servletPath = request.getServletPath();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(), servletPath,
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }
}
