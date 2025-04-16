package com.bnfd.overseer.model.constants;

public enum ActionCategory {
    LIBRARY;

    public static ActionCategory findByName(String name) {
        ActionCategory result = null;
        for (ActionCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                result = category;
                break;
            }
        }

        return result;
    }
}
