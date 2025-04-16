package com.bnfd.overseer.model.constants;

public enum BuilderCategory {
    //    MEDIA,
//    LABEL,
//    FILTER,
    LIST;
    //    TRACKER,
//    DATA,
//    CUSTOM;

    public static BuilderCategory findByName(String name) {
        BuilderCategory result = null;
        for (BuilderCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                result = category;
                break;
            }
        }

        return result;
    }
}
