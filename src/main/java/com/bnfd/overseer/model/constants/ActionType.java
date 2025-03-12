package com.bnfd.overseer.model.constants;

public enum ActionType {
    STRING,
    BOOLEAN,
    CRON;

    public static ActionType findByName(String name) {
        ActionType result = null;
        for (ActionType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
