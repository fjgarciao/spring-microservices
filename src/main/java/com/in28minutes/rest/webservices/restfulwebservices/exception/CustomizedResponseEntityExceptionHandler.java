package com.in28minutes.rest.webservices.restfulwebservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = getExceptionResponse(ex, request);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleUserNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = getExceptionResponse(ex, request);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    private ExceptionResponse getExceptionResponse(Exception ex, WebRequest request) {
        return new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
    }
}
