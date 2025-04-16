package com.bnfd.overseer.model.constants;

public enum MediaType {
    MOVIE,
    SERIES,
    SEASON,
    EPISODE;

    public static MediaType findByName(String name) {
        MediaType result = null;
        for (MediaType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
