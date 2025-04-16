package com.bnfd.overseer.model.constants;

public enum MetadataType {
    TITLE,
    TITLE_SORT,
    ORIGINAL_TITLE,
    SUMMARY,
    TAGLINE,
    RELEASE_DATE,
    POSTER,
    NUMBER,
    COUNT,
    EXTERNAL_ID;

    public static MetadataType findByName(String name) {
        MetadataType result = null;
        for (MetadataType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
