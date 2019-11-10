package com.secureally.demo.exceptions;

import com.secureally.demo.utils.AppliationConstraintError;
import com.secureally.demo.utils.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.secureally.demo.constants.ApplicationErrorCodes.CONSTRAINT_VIOLATION;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleFallThroughException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object>  handleConstraintViolation(final DataIntegrityViolationException ex, WebRequest request) {
        final List<AppliationConstraintError> errors = new ArrayList();

        String field = "";
        String message = ex.getMessage();
        errors.add(new AppliationConstraintError(field, message));
        ResponsePage body = new ResponsePage(HttpStatus.CONFLICT.value(), new MovieManagerException("Constraint Violations", CONSTRAINT_VIOLATION), errors);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);

    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = MovieManagerException.class)
    public ResponseEntity<Object> handleMovieManagerException(MovieManagerException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex, new HttpHeaders(), ex.getStatus(), request);
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        Object body = handleConstraintViolation(ex);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    /**
     *
     * @param ex
     * @return
     */
    public static ResponsePage handleConstraintViolation(final ConstraintViolationException ex) {
        LOGGER.error("Constraint Violoation Caught: {}", ex.getMessage());
        final List<AppliationConstraintError> errors = new ArrayList();

        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String searchPattern = violation.getPropertyPath().toString();
            String field = searchPattern.substring(searchPattern.lastIndexOf('.') + 1);
            String message = violation.getMessage();
            errors.add(new AppliationConstraintError(field, message));
        }

        return new ResponsePage(HttpStatus.BAD_REQUEST.value(), new MovieManagerException("Constraint Violations", CONSTRAINT_VIOLATION), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return handleExceptionInternal(ex, FieldsNotValidApiResponse.from(ex, status), headers, status, request);
    }

    /**
     *
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if (body != null && ex instanceof MovieManagerException) {
            LOGGER.error("Application Exception : {}", ex.getMessage(), ex);
            body = new ResponsePage(status.value(), (MovieManagerException) ex);
            status = HttpStatus.BAD_REQUEST;
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
