package com.bnfd.overseer.utils;

import com.bnfd.overseer.model.api.Metadata;
import com.bnfd.overseer.model.constants.MediaIdType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListUtils {
    public static <T> List<T> shrinkTo(List<T> list, int newSize) {
        return list.subList(0, newSize - 1);
    }

    public static Set<String> addExternalIdsToMap(Map<MediaIdType, Set<String>> externalIds, Metadata metadata) {
        MediaIdType idType = ApiUtils.getMediaIdType(metadata.getValue());
        Set<String> idSet = externalIds.get(idType);

        if (idSet == null) {
            idSet = new HashSet<>();
        }

        idSet.add(ApiUtils.getMediaId(metadata.getValue()));
        return idSet;
    }
}
