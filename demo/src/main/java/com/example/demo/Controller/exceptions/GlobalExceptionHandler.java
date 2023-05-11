package com.example.demo.Controller.exceptions;

import com.example.demo.Controller.errors.GeneratedError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> body = Collections.singletonMap("error", "Todo not found");
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<Object>> errorsMap = new HashMap<>();
        List<Object> errors = new ArrayList<>();

        for (FieldError fError : ex.getBindingResult().getFieldErrors()) {
            GeneratedError error = new GeneratedError();
            error.setError(fError.getCode());
            error.setMessage(fError.getDefaultMessage());
            error.setDetail("Error located in " + "'" + fError.getField() + "'" + " field");
            errors.add(error);
        }
        errorsMap.put("errors", errors);

        return new ResponseEntity<>(errorsMap, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
