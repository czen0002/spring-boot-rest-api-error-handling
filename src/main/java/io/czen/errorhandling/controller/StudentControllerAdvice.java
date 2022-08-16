package io.czen.errorhandling.controller;

import io.czen.errorhandling.model.error.ErrorBuilder;
import io.czen.errorhandling.model.error.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ControllerAdvice
public class StudentControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Errors handleException(Exception e) {
        return new Errors().errorList(singletonList(ErrorBuilder.buildInternalServerError(e.getMessage())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public Errors handleHttpMessageNotReadableException() {
        return new Errors().errorList(singletonList(ErrorBuilder.buildBadRequestError("Invalid JSON request payload")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public Errors handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorBuilder.buildMethodArgumentNotValidErrors(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public Errors handleConstraintViolationException(ConstraintViolationException e) {
        return ErrorBuilder.buildConstraintViolationErrors(e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public Errors handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ErrorBuilder.buildMethodArgumentTypeMismatchError(e);
    }
}
