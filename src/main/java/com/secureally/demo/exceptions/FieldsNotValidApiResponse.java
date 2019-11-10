package com.secureally.demo.exceptions;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class FieldsNotValidApiResponse extends ApiErrorResponse {
    private final List<FieldNotValidApiResponse> errors;

    public FieldsNotValidApiResponse(Integer status, String message, List<FieldNotValidApiResponse> errors) {
        super(status, message);
        this.errors = errors;
    }

    public static FieldsNotValidApiResponse from(MethodArgumentNotValidException ex, HttpStatus status) {
        List<FieldNotValidApiResponse> fieldErrors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldNotValidApiResponse::from)
                .collect(toList());
        return new FieldsNotValidApiResponse(
                status.value(),
                "Bad request",
                fieldErrors
        );
    }

    @Data
    public static class FieldNotValidApiResponse {
        private final String field;

        private final String message;

        public static FieldNotValidApiResponse from(@NonNull FieldError fieldError) {
            return new FieldNotValidApiResponse(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}