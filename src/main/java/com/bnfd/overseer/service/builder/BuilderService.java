package com.bnfd.overseer.service.builder;

import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.constants.BuilderCategory;

import java.util.List;

public interface BuilderService {
    List<Media> processCollectionBuilder(String collectionId, BuilderCategory category);
}
