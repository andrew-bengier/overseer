package com.bnfd.overseer.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OverseerAPIException - HttpStatus.EXPECTATION_FAILED.
 */
public class OverseerAPIException extends OverseerException {
    // region - Class Variables -
    private static final HttpStatus statusCode = HttpStatus.EXPECTATION_FAILED;

    private static final String message = statusCode.getReasonPhrase();

    private final List<String> errors;
    // endregion - Class Variables -

    // region - Constructors -
    public OverseerAPIException() {
        this(message);
    }

    public OverseerAPIException(String message) {
        super(message);

        errors = Collections.emptyList();
    }

    public OverseerAPIException(String message, List<String> errors) {
        super(message);

        this.errors = errors;
    }

    public OverseerAPIException(String message, Throwable throwable) {
        super(message, throwable);

        errors = Collections.emptyList();
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
