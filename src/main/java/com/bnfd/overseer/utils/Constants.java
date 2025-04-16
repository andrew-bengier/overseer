package com.bnfd.overseer.utils;

public class Constants {
    // region - Settings -
    public static final String DEFAULT_SETTINGS_FILE = "src/main/resources/defaultSettings.properties";
    public static final String DEFAULT_SETTING_KEY = "default.settings";
    public static final String ACTIVE_SETTING = "active";
    //    public static final String LANGUAGE_SETTING = "language";
    public static final String ASSET_DIRECTORY_SETTING = "AssetDirectory";
//    public static final String COLLECTION_ORDER_DEFAULT_SETTING = "CollectionOrderDefault";
//    public static final String REMOVE_BELOW_MINIMUM_SETTING = "RemoveBelowMinimum";
//    public static final String REMOVE_NOT_SCHEDULED_SETTING = "RemoveNotScheduled";
//    public static final String REMOVE_NOT_MANAGED_SETTING = "RemoveNotManaged";
//    public static final String SHOW_NOT_MANAGED_SETTING = "ShowNotManaged";
//    public static final String SCHEDULE_SETTING = "Schedule";
//    public static final String SYNC_MODE_SETTING = "SyncMode";
    // endregion - Settings -

    // region - Plex Api -
    public static final String PLEX_TOKEN_PARAM = "?X-Plex-Token=";
    public static final String PLEX_ALL_URL = "/all";
    public static final String PLEX_LIBRARIES_URL = "/library/sections";
    public static final String PLEX_LIBRARY_URL = "/library/sections/{referenceId}";
    public static final String PLEX_LIBRARY_COLLECTIONS_URL = "/library/sections/{referenceId}/collections";
    public static final String PLEX_COLLECTIONS_URL = "/library/collection/{referenceId}";
    public static final String PLEX_MEDIA_ITEM_URL = "/library/metadata/{referenceId}";
    public static final String PLEX_INCLUDE_CHILDREN_URL = "/children";
    // endregion - Plex Api -
}
