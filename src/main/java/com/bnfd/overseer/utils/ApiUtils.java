package com.bnfd.overseer.utils;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.service.api.*;

import java.util.*;

public class ApiUtils {
    public static <T extends ApiService> ApiService retrieveService(ApiKeyType type, Class<T> serviceType, List<ApiService> apiServices, boolean throwError) {
        Optional<ApiService> service = apiServices.stream().filter(source -> source.getClass() == serviceType).findFirst();
        if (service.isEmpty() && throwError) {
            throw new OverseerPreConditionRequiredException("Service not currently enabled: " + type.name());
        }

        return service.orElse(null);
    }
}
