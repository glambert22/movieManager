package com.secureally.demo.exceptions;

import com.secureally.demo.constants.ApplicationErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import com.google.common.base.Strings;
import javax.transaction.NotSupportedException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MovieManagerException extends RuntimeException implements ApplicationErrorCodes {

    private final HttpStatus status;
    private final String errorCode;
    private final Map<String, Object[]> substitutions = new HashMap<>();

    /**
     * exceptions should always contain a non-null message
     */
    private MovieManagerException() throws NotSupportedException {
        throw new NotSupportedException("Not Supported Operation");
    }

    public MovieManagerException(@NonNull String message, @NonNull String errorCode) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, errorCode);
    }

    public MovieManagerException(@NonNull String message, @NonNull Throwable cause, @NonNull String errorCode) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, cause, errorCode);
    }

    public MovieManagerException(@NonNull String message, @NonNull HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = !Strings.isNullOrEmpty(errorCode) ? errorCode : UNCATALOGUED_ERROR;
    }

    public MovieManagerException(@NonNull String message, @NonNull HttpStatus status, @NonNull Throwable cause, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = !Strings.isNullOrEmpty(errorCode) ? errorCode : UNCATALOGUED_ERROR;
    }

    public void putSubstitution(Object... substitutions) {
        this.substitutions.put(errorCode, substitutions);
    }

    public Map<String, Object[]> getSubstitutions() {
        return substitutions;
    }
}
