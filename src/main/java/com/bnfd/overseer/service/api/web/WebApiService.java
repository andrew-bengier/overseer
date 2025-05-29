package com.bnfd.overseer.service.api.web;

import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.Media;

import java.util.List;

public interface WebApiService {
    List<Media> getMediaFromCollection(String collectionId);

    Media getMovie(String movieId);

    Media getSeries(String seriesId);

    List<Collection> searchCollections(String collectionName);
}
