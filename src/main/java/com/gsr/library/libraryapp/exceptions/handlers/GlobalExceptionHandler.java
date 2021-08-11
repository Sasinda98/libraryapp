package com.gsr.library.libraryapp.exceptions.handlers;

import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /***
     * Conform server thrown exceptions also to APIExceptionTemplate defined.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String servletPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate("Something went wrong", status.getReasonPhrase(), servletPath,
                status.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, status);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handleAPIException(HttpServletRequest request, ValidationException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String servletPath = request.getServletPath();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(), servletPath,
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }

    @ExceptionHandler(value = {OperationStoppedException.class})
    public ResponseEntity<Object> handleAPIException(HttpServletRequest request, OperationStoppedException ex){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String servletPath = request.getServletPath();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(), servletPath,
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<Object> handleAPIException(HttpServletRequest request, NoResourceFoundException ex){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String servletPath = request.getServletPath();
        APIExceptionTemplate exceptionDetailsToExpose = new APIExceptionTemplate(ex.getMessage(), httpStatus.getReasonPhrase(), servletPath,
                httpStatus.value(), new Date());
        return new ResponseEntity<>(exceptionDetailsToExpose, httpStatus);
    }
}
