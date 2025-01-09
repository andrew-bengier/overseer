package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.utils.*;
import org.apache.commons.lang3.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ValidationService {
    // region - Class Variables -
    private Class<? extends Throwable> errorType;
    private List<String> errors;
    // endregion - Class Variables -

    // TODO: add list of invalid errors
    public void validateApiKey(ApiKey apiKey, Integer id, boolean isNew) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(apiKey)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("ApiKey is empty or null"));
        }

        checkApiKeyMissingRequiredAttributes(apiKey, isNew);
        checkApiKeyInvalidAttributes(apiKey, id, isNew);

        if (ObjectUtils.isNotEmpty(errorType)) {
            ErrorCreator error = new ErrorCreator();
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkApiKeyMissingRequiredAttributes(ApiKey apiKey, boolean isNew) throws Throwable {
        if (!isNew) {
            if (ObjectUtils.isEmpty(apiKey.getId())) {
                errorType = OverseerBadRequestException.class;
                errors.add("Missing api key id");
            }
        }

        if (StringUtils.isBlank(apiKey.getName())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing api key type");
        }

        if (StringUtils.isBlank(apiKey.getKey())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing api key");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            ErrorCreator error = new ErrorCreator();
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkApiKeyInvalidAttributes(ApiKey apiKey, Integer id, boolean isNew) throws Throwable {
        if (isNew) {
            if (ObjectUtils.isNotEmpty(apiKey.getId())) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid api key non-null id");
            }
        } else {
            if (apiKey.getId() != id) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid api key non-matching id");
            }
        }

        if (ObjectUtils.isEmpty(ApiKeyType.findByName(apiKey.getName()))) {
            errorType = OverseerUnprocessableException.class;
            errors.add("Invalid api key type");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            ErrorCreator error = new ErrorCreator();
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
}
