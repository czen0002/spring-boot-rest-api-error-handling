package io.czen.errorhandling.model.error;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static java.util.Collections.singletonList;

public class ErrorBuilder {

    private static final String API_BAD_REQUEST = "BAD_REQUEST";
    private static final String API_INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    private static final String API_FORBIDDEN = "FORBIDDEN";
    private static final String API_INTERNAL_SERVER = "INTERNAL_SERVER";

    private ErrorBuilder() {}

    public static Error buildBasicError(String code, String title, String detail) {
        return new Error().code(code).title(title).detail(detail);
    }

    public static Error buildBadRequestError(String detail) {
        return buildBasicError(valueOf(BAD_REQUEST.value()), API_BAD_REQUEST, detail);
    }

    public static Error buildUnauthorizedError(String detail) {
        return buildBasicError(valueOf(UNAUTHORIZED.value()), API_INVALID_CREDENTIALS, detail);
    }

    public static Error buildForbiddenError(String detail) {
        return buildBasicError(valueOf(FORBIDDEN.value()), API_FORBIDDEN, detail);
    }

    public static Error buildInternalServerError(String detail) {
        return buildBasicError(valueOf(INTERNAL_SERVER_ERROR.value()), API_INTERNAL_SERVER, detail);
    }

    public static Errors buildMethodArgumentNotValidErrors(MethodArgumentNotValidException e) {
        return new Errors().errorList(e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorBuilder::fromFieldError)
                .toList());
    }

    public static Errors buildConstraintViolationErrors(ConstraintViolationException e) {
        return new Errors().errorList(e.getConstraintViolations()
                .stream()
                .map(ErrorBuilder::fromConstraintViolation)
                .toList());
    }

    public static Errors buildMethodArgumentTypeMismatchError(MethodArgumentTypeMismatchException e) {
        Class<?> requiredType = e.getRequiredType();
        String requiredTypeName = requiredType == null ? "" : requiredType.getName();
        String detail = String.format("Parameter '%s' is invalid. Expected a valid %s, but received %s.", e.getName(),
                requiredTypeName, e.getValue().getClass());
        return new Errors().errorList(singletonList(buildBasicError(valueOf(BAD_REQUEST.value()), API_BAD_REQUEST, detail)));
    }

    private static Error fromFieldError(FieldError fieldError) {
        return buildBadRequestError(fieldError.getDefaultMessage());
    }

    private static Error fromConstraintViolation(ConstraintViolation<?> constraintViolation) {
        return buildBadRequestError(constraintViolation.getMessage());
    }
}
