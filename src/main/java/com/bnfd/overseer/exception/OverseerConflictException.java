package com.bnfd.overseer.exception;

import com.google.gson.*;
import org.springframework.http.*;

import java.util.*;

/**
 * OverseerConflictException - HttpStatus.CONFLICT.
 */
public class OverseerConflictException extends RuntimeException {
    // region - Class Variables -
    private static final HttpStatus statusCode = HttpStatus.CONFLICT;

    private static final String message = statusCode.getReasonPhrase();

    private final List<String> errors;
    // endregion - Class Variables -

    // region - Constructors -
    public OverseerConflictException() {
        this(message);
    }

    public OverseerConflictException(String message) {
        super(message);

        this.errors = Collections.emptyList();
    }

    public OverseerConflictException(List<String> errors) {
        super(message);

        this.errors = errors;
    }

    public OverseerConflictException(String message, List<String> errors) {
        super(message);

        this.errors = errors;
    }

    public OverseerConflictException(String message, Throwable throwable) {
        super(message, throwable);

        this.errors = Collections.emptyList();
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public static HttpStatus getStatusCode() {
        return statusCode;
    }

    public List<String> getErrors() {
        return errors;
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public String toString() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", getMessage());
        body.put("errors", getErrors());
        body.put("status", getStatusCode());

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(body);
    }
    // endregion - Overridden Methods -
}
