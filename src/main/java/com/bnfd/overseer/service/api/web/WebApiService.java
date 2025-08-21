package com.bnfd.overseer.service.api.web;

import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.service.ApiKeyService;

import java.util.List;

public interface WebApiService {
    boolean isEnabled();

    void enableService(ApiKeyService apiKeyService);
    
    List<Media> getMediaFromCollection(String collectionId);

    Media getMovie(String movieId);

    Media getSeries(String seriesId);

    List<Collection> searchCollections(String collectionName);
}
