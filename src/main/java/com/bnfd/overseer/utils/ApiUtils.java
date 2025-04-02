package com.bnfd.overseer.utils;

import com.bnfd.overseer.exception.OverseerPreConditionRequiredException;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.service.api.media.server.MediaServerApiService;

import java.util.List;
import java.util.Optional;

public class ApiUtils {
    public static <T extends MediaServerApiService> MediaServerApiService retrieveService(ApiKeyType type, Class<T> serviceType, List<MediaServerApiService> mediaServerApiServices, boolean throwError) {
        Optional<MediaServerApiService> service = mediaServerApiServices.stream().filter(source -> source.getClass() == serviceType).findFirst();
        if (service.isEmpty() && throwError) {
            throw new OverseerPreConditionRequiredException("Service not currently enabled: " + type.name());
        }

        return service.orElse(null);
    }
}
