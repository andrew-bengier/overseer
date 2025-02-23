package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.utils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;

@Service
public class ValidationService {
    // region - Class Variables -
    private Class<? extends Throwable> errorType;
    private List<String> errors;
    // endregion - Class Variables -

    // TODO: add list of invalid errors
    // region - ApiKey -
    public void validateApiKey(ApiKey apiKey, String id, boolean isNew) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(apiKey)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("ApiKey is empty or null"));
        }

        checkApiKeyMissingRequiredAttributes(apiKey, isNew);
        checkApiKeyInvalidAttributes(apiKey, id, isNew);
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
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkApiKeyInvalidAttributes(ApiKey apiKey, String id, boolean isNew) throws Throwable {
        if (isNew) {
            if (ObjectUtils.isNotEmpty(apiKey.getId())) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid api key non-null id");
            }
        } else {
            if (!apiKey.getId().equalsIgnoreCase(id)) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid api key non-matching id");
            }
        }

        if (ObjectUtils.isEmpty(ApiKeyType.findByName(apiKey.getName()))) {
            errorType = OverseerUnprocessableException.class;
            errors.add("Invalid api key type");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
    // endregion - ApiKey -

    // region - Server -
    public void validateServer(Server server, String id, boolean isNew) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(server)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("Server is empty or null"));
        }

        checkServerMissingRequiredAttributes(server, isNew);
        checkServerInvalidAttributes(server, id, isNew);
    }

    protected void checkServerMissingRequiredAttributes(Server server, boolean isNew) throws Throwable {
        if (!isNew) {
            if (ObjectUtils.isEmpty(server.getId())) {
                errorType = OverseerBadRequestException.class;
                errors.add("Missing server id");
            }

            // required settings
            if (CollectionUtils.isEmpty(server.getSettings())) {
                errorType = OverseerBadRequestException.class;
                errors.add("Missing all server settings");
            } else {
                if (!server.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent()) {
                    errorType = OverseerBadRequestException.class;
                    errors.add("Missing server setting - active");
                }
            }
        }

        if (StringUtils.isBlank(server.getName())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing server name");
        }

        if (ObjectUtils.isEmpty(server.getApiKey())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing server api key");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkServerInvalidAttributes(Server server, String id, boolean isNew) throws Throwable {
        if (isNew) {
            if (ObjectUtils.isNotEmpty(server.getId())) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid server non-null id");
            }
        } else {
            if (!server.getId().equalsIgnoreCase(id)) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid server non-matching id");
            }
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
    // endregion - Server -

    // region - Settings -

    // endregion - Settings -
}
