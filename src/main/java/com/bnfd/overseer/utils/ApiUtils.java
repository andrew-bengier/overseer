package com.bnfd.overseer.utils;

import com.bnfd.overseer.exception.OverseerPreConditionRequiredException;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.constants.BuilderType;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.constants.MetadataType;
import com.bnfd.overseer.service.ApiKeyService;
import com.bnfd.overseer.service.api.media.server.MediaServerApiService;
import com.bnfd.overseer.service.api.web.WebApiService;
import com.bnfd.overseer.service.builder.BuilderService;

import java.util.List;
import java.util.Optional;

public class ApiUtils {
    public static <T extends MediaServerApiService> MediaServerApiService retrieveMediaApiService(ApiKeyType type, Class<T> serviceType, List<MediaServerApiService> mediaServerApiServices, boolean throwError) {
        Optional<MediaServerApiService> service = mediaServerApiServices.stream().filter(source -> source.getClass() == serviceType).findFirst();
        if (service.isEmpty() && throwError) {
            throw new OverseerPreConditionRequiredException("Service not currently enabled: " + type.name());
        }

        return service.orElse(null);
    }

    public static <T extends WebApiService> WebApiService retrieveWebApiService(BuilderType type, Class<T> serviceType, ApiKeyService apiKeyService, List<WebApiService> webApiServices, boolean throwError) {
        Optional<WebApiService> service = webApiServices.stream().filter(source -> source.getClass() == serviceType).findFirst();
        if (service.isEmpty() && throwError) {
            throw new OverseerPreConditionRequiredException("Service not currently enabled: " + type.name());
        }

        WebApiService api = service.orElse(null);

        if (api != null && !api.isEnabled()) {
            api.enableService(apiKeyService);
        }

        return api;
    }

    public static <T extends BuilderService> BuilderService retrieveBuilderService(BuilderType type, Class<T> serviceType, List<BuilderService> builderServices, boolean throwError) {
        Optional<BuilderService> service = builderServices.stream().filter(source -> source.getClass() == serviceType).findFirst();
        if (service.isEmpty() && throwError) {
            throw new OverseerPreConditionRequiredException("Service not currently enabled: " + type.name());
        }

        return service.orElse(null);
    }

    public static MediaIdType getMediaIdType(String externalId) {
        String type = externalId.substring(0, externalId.indexOf("_"));
        return MediaIdType.findByName(type);
    }

    public static String getMediaId(String externalId) {
        return externalId.substring(externalId.indexOf("_") + 1);
    }

    public static MetadataType getMetadataType(String type) {
        for (MetadataType metadataType : MetadataType.values()) {
            if (type.toLowerCase().contains(metadataType.name().toLowerCase())) {
                return metadataType;
            }
        }

        return null;
    }
}
