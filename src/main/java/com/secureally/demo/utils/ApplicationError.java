package com.secureally.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.secureally.demo.constants.ApplicationConstants;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

import com.secureally.demo.exceptions.MovieManagerException;
import lombok.Data;
import lombok.NonNull;



@Data
public class ApplicationError implements ApplicationConstants {
    private static ObjectMapper MAPPER;
    private static ApplicationMessage CATALOG_MESSAGE;

    private final int status;
    private final String code;
    private final String message;
    private final Instant timestamp = Instant.now();

    @JsonInclude(Include.NON_NULL)
    private final List<AppliationConstraintError> errors;

    public static void setStaticMembers(ObjectMapper mapper, ApplicationMessage message) {
        MAPPER = mapper;
        CATALOG_MESSAGE = message;
    }

    /**
     * Builds a JSON object with the following constructs:
     *
     * <code>
     * { "status": "401", "code": "E401", "message": "Not authorized to access this resource", }
     * </code>
     */
    public static JsonNode error(int status, String code) {
        ObjectNode error = MAPPER.createObjectNode();

        /* status */
        error.put(HTTP_STATUS, status);

        /* error code */
        error.put(CODE, code);

        /* message */
        error.put(MESSAGE, CATALOG_MESSAGE.get(code));

        error.put(TIMESTAMP, Instant.now().toString());

        return error;
    }

    public static ApplicationError of(int status, @NonNull MovieManagerException te, List<AppliationConstraintError> errors) {
        if (te.getSubstitutions().size() > 0) {
            Object [] substitutions = te.getSubstitutions().get(te.getErrorCode());
            return new ApplicationError(status, te.getErrorCode(), MessageFormat.format(CATALOG_MESSAGE.get(te.getErrorCode()), substitutions), errors);
        }
        return new ApplicationError(status, te.getErrorCode(), CATALOG_MESSAGE.get(te.getErrorCode()), errors);
    }
}
