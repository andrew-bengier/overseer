package com.bnfd.overseer.service.builder;

import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.api.Metadata;
import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.MetadataType;
import com.bnfd.overseer.service.api.web.TmdbWebApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TmdbBuilderService implements BuilderService {
    // region - Class Variables -
    private final TmdbWebApiService tmdbWebApiService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public TmdbBuilderService(TmdbWebApiService tmdbWebApiService) {
        this.tmdbWebApiService = tmdbWebApiService;
    }

    // endregion - Constructors -

    @Override
    public List<Media> processCollectionBuilder(String collectionId, BuilderCategory category) {

        log.info("Processing tmdb collection builder {}", collectionId);

        List<Media> parts = tmdbWebApiService.getMediaFromCollection(collectionId);
        for (Media media : parts) {
            Metadata title = media.getMetadata().stream().filter(metadata -> metadata.getName().equalsIgnoreCase(MetadataType.TITLE.name())).findFirst().orElse(new Metadata(null, null, ""));
            Metadata externalImdbId = media.getMetadata().stream().filter(metadata -> metadata.getName().equalsIgnoreCase(MetadataType.EXTERNAL_ID.name())).findFirst().orElse(new Metadata(null, null, ""));
            log.info("Media in collection {} - {}", externalImdbId.getValue(), title.getValue());
        }

        // return media not currently in media server
        return Collections.emptyList();
    }
}
