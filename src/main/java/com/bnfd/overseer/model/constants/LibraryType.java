package com.bnfd.overseer.model.constants;

public enum LibraryType {
    MOVIE,
    SHOW;

    public static LibraryType findByName(String name) {
        LibraryType result = null;
        for (LibraryType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
