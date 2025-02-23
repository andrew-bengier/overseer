package com.bnfd.overseer.exception;

import com.google.gson.*;
import org.springframework.http.*;

import java.util.*;

/**
 * OverseerBadRequestException - HttpStatus.PRECONDITION_REQUIRED.
 */
public class OverseerPreConditionRequiredException extends RuntimeException {
    // region - Class Variables -
    private static final HttpStatus statusCode = HttpStatus.PRECONDITION_REQUIRED;

    private static final String message = statusCode.getReasonPhrase();

    private final List<String> errors;
    // endregion - Class Variables -

    // region - Constructors -
    public OverseerPreConditionRequiredException() {
        this(message);
    }

    public OverseerPreConditionRequiredException(String message) {
        super(message);

        this.errors = Collections.emptyList();
    }

    public OverseerPreConditionRequiredException(List<String> errors) {
        super(message);

        this.errors = errors;
    }

    public OverseerPreConditionRequiredException(String message, List<String> errors) {
        super(message);

        this.errors = errors;
    }

    public OverseerPreConditionRequiredException(String message, Throwable throwable) {
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
