package com.bnfd.overseer.service.api.media.server;

import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import com.bnfd.overseer.model.persistence.LibraryEntity;
import com.bnfd.overseer.model.persistence.ServerEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface MediaServerApiService {
    List<LibraryEntity> getLibraries(ServerEntity server);

    List<Media> getMedia(ApiKeyEntity apiKey, String libraryId, Map<MediaIdType, List<String>> mediaIds) throws UnsupportedEncodingException;

//    Media getMedia(Server server, String libraryId, String imdbId) throws UnsupportedEncodingException;
}
