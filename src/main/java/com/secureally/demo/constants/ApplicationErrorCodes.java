package com.secureally.demo.constants;

public interface ApplicationErrorCodes {
    static final String INVALID_MOVIE_ERROR = "E418";
    static final String CONSTRAINT_VIOLATION = "E450";
    static final String INVALID_MOVIE_ATTRIBUTE = "E451";

    // FALL THROUGH
    static final String UNCATALOGUED_ERROR = "E500";
}
