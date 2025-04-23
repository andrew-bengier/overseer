package com.bnfd.overseer.model.constants;

public enum CollectionTrackingType {
    TRACKED,
    TRACKED_INFO,
    UNTRACKED,
    UNTRACKED_INFO,
    ALL,
    ALL_INFO;

    public static CollectionTrackingType findByName(String name) {
        CollectionTrackingType result = null;
        for (CollectionTrackingType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
