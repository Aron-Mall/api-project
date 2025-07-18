package com.armal.project.controller;

import com.armal.project.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormFinalizedExecption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleFormFinalizedException(FormFinalizedExecption e) {
        return new ResponseEntity<>("Cannot modify a finalized form", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormSequenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleFormSequenceException(FormSequenceException e) {
        return new ResponseEntity<>("Form Sequence error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<Object> handleNoAccessException(NoAccessException e) {
        return new ResponseEntity<>("No access", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoAddressFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> handleNoAddressFoundException(NoAddressFoundException e) {
        return new ResponseEntity<>("No Address found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoFormFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> handleNoFormFoundException(NoFormFoundException e) {
        return new ResponseEntity<>("No Form found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FormIncompleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleFormIncompleteException(FormIncompleteException e) {
        return new ResponseEntity<>("Incomplete Form", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoPersonalDetailsFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> handleNoPersoanlDetailsFoundException(NoPersonalDetailsFoundException e) {
        return new ResponseEntity<>("No Personal detials found", HttpStatus.NOT_FOUND);
    }





}
