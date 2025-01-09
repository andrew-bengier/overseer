package com.bnfd.overseer.exception;

import com.google.gson.*;
import org.springframework.http.*;

import java.util.*;

/**
 * OverseerException.
 * If no subclass is provided, the defaults to HttpStatus.INTERNAL_SERVER_ERROR
 */
public class OverseerException extends RuntimeException {
    // region - Class Variables -
    private final HttpStatus statusCode;

    private final String message;

    private final List<String> errors;
    // endregion - Class Variables -

    // region - Constructors -
    public OverseerException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public OverseerException(String message) {
        super(message);

        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.errors = Collections.emptyList();
    }

    public OverseerException(String message, List<String> errors) {
        super(message);

        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.errors = errors;
    }

    public OverseerException(String message, Throwable throwable) {
        super(message, throwable);

        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.errors = Collections.emptyList();
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public HttpStatus getStatusCode() {
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
