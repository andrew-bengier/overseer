package com.bnfd.overseer.service.api.media.server;

import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.media.plex.MediaContainer;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import com.bnfd.overseer.model.persistence.CollectionEntity;
import com.bnfd.overseer.model.persistence.LibraryEntity;
import com.bnfd.overseer.model.persistence.ServerEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MediaServerApiService {
    // region - GET -
    List<LibraryEntity> getLibraries(ServerEntity server);

    List<CollectionEntity> getCollections(ApiKeyEntity apiKey, String libraryId, boolean includeMedia);

    List<Media> getMedia(ApiKeyEntity apiKey, String libraryId, Map<MediaIdType, Set<String>> mediaIds) throws UnsupportedEncodingException;

    MediaContainer getMedia(ApiKeyEntity apiKey, String libraryId) throws UnsupportedEncodingException;

    Media getMedia(ApiKeyEntity apiKey, String libraryId, String mediaId) throws UnsupportedEncodingException;

//    Media getMedia(Server server, String libraryId, String imdbId) throws UnsupportedEncodingException;
    // endregion - GET -

    // region - PUT -
    void createOrUpdateCollection(ApiKeyEntity apiKey, CollectionEntity collection, Set<String> mediaIds);
    // endregion - PUT -
}
