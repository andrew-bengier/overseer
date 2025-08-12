package com.bnfd.overseer.service;

import com.bnfd.overseer.config.DefaultSettings;
import com.bnfd.overseer.exception.ErrorCreator;
import com.bnfd.overseer.exception.OverseerBadRequestException;
import com.bnfd.overseer.exception.OverseerUnprocessableException;
import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.Library;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.utils.ValidationUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ValidationService {
    // region - Class Variables -
    private Class<? extends Throwable> errorType;
    private List<String> errors;
    // endregion - Class Variables -

    // region - ApiKey -
    public static List<ApiKeyType> getRequiredApiKeys() {
        return List.of(ApiKeyType.PLEX, ApiKeyType.TMDB);
    }

    public void validateApiKeySearchParams(Map<String, String> searchParams) throws Throwable {
        if (StringUtils.isBlank(searchParams.get("type"))) {
            throw new OverseerBadRequestException(List.of("Missing search param - type"));
        }

        try {
            ApiKeyType apiKeyType = ApiKeyType.valueOf(searchParams.get("type").toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new OverseerBadRequestException(List.of("Invalid search param - type"));
        }
    }

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

        if (ObjectUtils.isEmpty(apiKey)) {
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

    // region - Library -
    public void validateLibrary(Library library, String id, boolean isNew) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(library)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("Library is empty or null"));
        }

        checkLibraryMissingRequiredAttributes(library, isNew);
        checkLibraryInvalidAttributes(library, id, isNew);
    }

    protected void checkLibraryMissingRequiredAttributes(Library library, boolean isNew) throws Throwable {
        if (!isNew) {
            if (ObjectUtils.isEmpty(library.getId())) {
                errorType = OverseerBadRequestException.class;
                errors.add("Missing library id");
            }
        }

        if (StringUtils.isBlank(library.getName())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing library name");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkLibraryInvalidAttributes(Library library, String id, boolean isNew) throws Throwable {
        if (isNew) {
            if (ObjectUtils.isNotEmpty(library.getId())) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid library non-null id");
            }
        } else {
            if (!library.getId().equalsIgnoreCase(id)) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid library non-matching id");
            }
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
    // endregion - Library -

    // region - Collection -
    public void validateCollection(Collection collection, String id, boolean isNew) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(collection)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("Collection is empty or null"));
        }

        checkCollectionMissingRequiredAttributes(collection, isNew);
        checkCollectionInvalidAttributes(collection, id, isNew);
    }

    public void validateCollectionForProcessing(Collection collection) throws Throwable {
        errorType = null;
        errors = new ArrayList<>();

        if (ValidationUtils.isEmpty(collection)) {
            throw ErrorCreator.createThrowable(OverseerBadRequestException.class, List.of("Collection is empty or null"));
        }

        if (StringUtils.isBlank(collection.getLibraryId())) {
            errorType = OverseerUnprocessableException.class;
            errors.add("Missing collection library id");
        }

        if (StringUtils.isBlank(collection.getReferenceId())) {
            errorType = OverseerUnprocessableException.class;
            errors.add("Missing collection reference id");
        }

        if (StringUtils.isBlank(collection.getName())) {
            errorType = OverseerUnprocessableException.class;
            errors.add("Missing collection name");
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkCollectionMissingRequiredAttributes(Collection collection, boolean isNew) throws Throwable {
        if (!isNew) {
            if (ObjectUtils.isEmpty(collection.getId())) {
                errorType = OverseerBadRequestException.class;
                errors.add("Missing collection id");
            }
        }

        if (StringUtils.isBlank(collection.getName())) {
            errorType = OverseerBadRequestException.class;
            errors.add("Missing collection name");
        }

        // Add builders check - at least 1 builder and attributes for builder, then later check for builder validation

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }

    protected void checkCollectionInvalidAttributes(Collection collection, String id, boolean isNew) throws Throwable {
        if (isNew) {
            if (ObjectUtils.isNotEmpty(collection.getId())) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid collection non-null id");
            }
        } else {
            if (!collection.getId().equalsIgnoreCase(id)) {
                errorType = OverseerUnprocessableException.class;
                errors.add("Invalid collection non-matching id");
            }
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
    // endregion - Collection -

    // region - Settings -
    // region -- Default Settings --
    public void validateDefaultSettingsUpdateRequest(String level, Set<String> keys) throws Throwable {
        if (ObjectUtils.isEmpty(SettingLevel.findByName(level))) {
            errorType = OverseerUnprocessableException.class;
            errors.add(String.format("Invalid setting level: %s", level));
        }

        Map<String, String> defaultSettings = DefaultSettings.getSettings().get(SettingLevel.DEFAULT.name().toLowerCase());
        for (String key : keys) {
            if (!defaultSettings.containsKey(key)) {
                errorType = OverseerBadRequestException.class;
                errors.add(String.format("Invalid default setting: %s", key));
            }
        }

        if (ObjectUtils.isNotEmpty(errorType)) {
            throw ErrorCreator.createThrowable(errorType, errors);
        }
    }
    // endregion -- Default Settings --
    // endregion - Settings --

    // region - Media -
//    public String getSeriesStatus(String seriesId) {
//
//    }
    // endregion - Media -
}
