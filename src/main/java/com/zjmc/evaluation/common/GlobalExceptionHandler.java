package com.zjmc.evaluation.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Result<Object> result = new Result<>();
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Validation Failed");
        result.setData(errors);
        logger.error("Validation error: {}", errors, ex);
        return result;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        Result<Object> result = new Result<>();
        result.setCode(HttpStatus.NOT_FOUND.value());
        result.setMessage(ex.getMessage());
        logger.error("Entity not found: {}", ex.getMessage(), ex);
        return result;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<Object> handleRuntimeException(RuntimeException ex) {
        Result<Object> result = new Result<>();
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setMessage(ex.getMessage());
        logger.error("Runtime exception: {}", ex.getMessage(), ex);
        return result;
    }
}
