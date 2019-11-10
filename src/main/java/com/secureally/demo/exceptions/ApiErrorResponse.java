package com.secureally.demo.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
public class ApiErrorResponse {
    private final Instant timestamp = Instant.now();

    private final Integer status;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    public static ApiErrorResponse from(@NonNull MovieManagerException ex) {
        return new ApiErrorResponse(ex.getStatus().value(), ex.getMessage());
    }
}