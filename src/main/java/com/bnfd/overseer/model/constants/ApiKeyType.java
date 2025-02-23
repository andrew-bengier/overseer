package com.bnfd.overseer.model.constants;

public enum ApiKeyType {
    PLEX,
    TMDB;

    public static ApiKeyType findByName(String name) {
        ApiKeyType result = null;
        for (ApiKeyType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
