package com.bnfd.overseer.model.constants;

public enum SettingType {
    STRING,
    BOOLEAN,
    NUMBER,
    DATE,
    CRON;

    public static SettingType findByName(String name) {
        SettingType result = null;
        for (SettingType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
