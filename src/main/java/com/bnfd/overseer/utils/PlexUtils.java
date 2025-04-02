package com.bnfd.overseer.utils;

import com.bnfd.overseer.model.constants.MetadataType;
import com.bnfd.overseer.model.media.plex.components.Guid;
import com.bnfd.overseer.model.persistence.CollectionEntity;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlexUtils {
    public static boolean guidsContainFilter(List<Guid> guids, String filter) {
        return CollectionUtils.isNotEmpty(guids) && guids.stream().anyMatch(guid -> guid.getId().equalsIgnoreCase(filter));
    }

    public static Map<String, String> getCollectionParams(List<String> mediaIds, CollectionEntity collection) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("includeExternalMedia", "1");
        requestParams.put("id", String.join(",", mediaIds));
        requestParams.put("type", "18");

//        if (CollectionUtils.isNotEmpty(collection.getMetadata())) {
//            collection.getMetadata().stream().filter(metadata -> metadata.getType() == MetadataType.TITLE).findFirst();
//            requestParams.put("title.value",
//        }

        return requestParams;
    }

    public static Map<String, String> getCollectionItemsParams(List<String> mediaIds, CollectionEntity collection) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("includeExternalMedia", "1");
        requestParams.put("id", String.join(",", mediaIds));
        requestParams.put("type", "1");
        requestParams.put("collection.locked", "1");
        requestParams.put("collection[0].tag.tag", collection.getName());

        return requestParams;
    }

    public static String getPlexMetadata(MetadataType metadataType, boolean locked) {
        String type = metadataType.name().toLowerCase();
        if (locked) {
            type += "_locked";
        } else {
            type += "_value";
        }
        return type.replace("_", ".");
    }
}
