package com.bnfd.overseer.utils;

public class Constants {
    // region - Settings -
    public static String ACTIVE_SETTING = "active";
    //    public static String LANGUAGE_SETTING = "language";
    public static String ASSET_DIRECTORY_SETTING = "AssetDirectory";
//    public static String COLLECTION_ORDER_DEFAULT_SETTING = "CollectionOrderDefault";
//    public static String REMOVE_BELOW_MINIMUM_SETTING = "RemoveBelowMinimum";
//    public static String REMOVE_NOT_SCHEDULED_SETTING = "RemoveNotScheduled";
//    public static String REMOVE_NOT_MANAGED_SETTING = "RemoveNotManaged";
//    public static String SHOW_NOT_MANAGED_SETTING = "ShowNotManaged";
//    public static String SCHEDULE_SETTING = "Schedule";
//    public static String SYNC_MODE_SETTING = "SyncMode";
    // endregion - Settings -

    // region - Plex Api -
    public static String PLEX_TOKEN_PARAM = "?X-Plex-Token=";
    public static String PLEX_LIBRARIES_URL = "/library/sections";
    public static String PLEX_LIBRARY_ITEMS_URL = "/library/sections/{referenceId}/all";
    public static String PLEX_COLLECTIONS_URL = "/library/sections/{referenceId}/collections";
    public static String PLEX_MEDIA_ITEM_URL = "library/metadata/{id}";
    public static String PLEX_INCLUDE_CHILDREN_URL = "/children";
    // endregion - Plex Api -
}
