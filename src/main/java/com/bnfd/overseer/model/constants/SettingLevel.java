package com.bnfd.overseer.model.constants;

public enum SettingLevel {
    DEFAULT,
    SERVER,
    LIBRARY,
    COLLECTION;

    public static SettingLevel findByName(String name) {
        SettingLevel result = null;
        for (SettingLevel type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
