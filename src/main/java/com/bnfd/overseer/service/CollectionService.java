package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.persistence.*;
import com.bnfd.overseer.repository.*;
import com.bnfd.overseer.service.api.media.server.MediaServerApiService;
import com.bnfd.overseer.service.api.media.server.PlexMediaServerApiService;
import com.bnfd.overseer.service.api.web.TmdbWebApiService;
import com.bnfd.overseer.service.api.web.WebApiService;
import com.bnfd.overseer.service.builder.BuilderService;
import com.bnfd.overseer.service.builder.TmdbBuilderService;
import com.bnfd.overseer.utils.*;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.bnfd.overseer.model.constants.ApiKeyType.PLEX;
import static com.bnfd.overseer.model.constants.BuilderType.TMDB;

@Slf4j
@Service
public class CollectionService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final CollectionRepository collectionRepository;
    private final CollectionBuilderRepository collectionBuilderRepository;
    private final SettingRepository settingRepository;
    private final ActionRepository actionRepository;

    private final ApiKeyRepository apiKeyRepository;

    private final List<MediaServerApiService> mediaServerApiServices;
    private final List<WebApiService> webApiServices;
    private final List<BuilderService> builderServices;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public CollectionService(
            @Qualifier("overseer-mapper") ModelMapper overseerMapper,
            CollectionRepository collectionRepository,
            CollectionBuilderRepository collectionBuilderRepository,
            SettingRepository settingRepository,
            ActionRepository actionRepository,
            ApiKeyRepository apiKeyRepository,
            List<MediaServerApiService> mediaServerApiServices,
            List<BuilderService> builderServices,
            List<WebApiService> webApiServices) {
        this.overseerMapper = overseerMapper;
        this.collectionRepository = collectionRepository;
        this.collectionBuilderRepository = collectionBuilderRepository;
        this.settingRepository = settingRepository;
        this.actionRepository = actionRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.mediaServerApiServices = mediaServerApiServices;
        this.builderServices = builderServices;
        this.webApiServices = webApiServices;
    }
    // endregion - Constructors -

    // region - Process -
    @Transactional
    public Collection processCollectionById(Server server, String libraryId, String collectionId) throws UnsupportedEncodingException {
        Optional<CollectionEntity> entity = collectionRepository.findById(collectionId);

        if (entity.isEmpty()) {
            throw new OverseerNotFoundException(String.format("No collection found for provided criteria (collectionId: %s)", collectionId));
        }

        CollectionEntity collectionEntity = entity.get();
        collectionEntity.getSettings().forEach(setting -> {
            log.debug("Processing setting {}", setting);
        });

        collectionEntity.getActions().forEach(action -> {
            log.debug("Processing action {}", action);
        });

        List<Media> media = new ArrayList<>();
        collectionEntity.getBuilders().forEach(collectionBuilder -> {
            log.debug("Processing builder {} {} - {}: {}", collectionBuilder.getType(), collectionBuilder.getCategory(), collectionBuilder.getName(), String.join(", ", collectionBuilder.getBuilderAttributes()));
            // TODO: add checks here for settings & actions (overridable / changeable)

            BuilderService builderService;
            switch (collectionBuilder.getType()) {
                case TMDB -> {
                    builderService = ApiUtils.retrieveBuilderService(TMDB, TmdbBuilderService.class, builderServices, true);
                    // TODO: add check here for builderCategory
                    for (String builderAttribute : collectionBuilder.getBuilderAttributes()) {
                        media.addAll(builderService.processCollectionBuilder(builderAttribute, collectionBuilder.getCategory()));
                        log.info("{} processed", builderAttribute);
                    }
                }
                default -> throw new OverseerException("Error - service for server type not currently supported");
            }
        });

        // check media server for media
        MediaServerApiService mediaService;
        ApiKeyEntity apiKey = overseerMapper.map(server.getApiKey(), ApiKeyEntity.class);
        switch (apiKey.getType()) {
            case PLEX ->
                    mediaService = ApiUtils.retrieveMediaApiService(PLEX, PlexMediaServerApiService.class, mediaServerApiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }
        // - check if media exists in media server (if so, is it already in collection, if not update to add)
        Map<MediaIdType, Set<String>> externalIds = new HashMap<>();
        for (Media part : media) {
            part.getMetadata()
                    .stream()
                    .filter(metadata -> metadata.getName().equalsIgnoreCase(MetadataType.EXTERNAL_ID.name()))
                    .findFirst()
                    .ifPresent(metadata -> {
                        externalIds.put(ApiUtils.getMediaIdType(metadata.getValue()), ListUtils.addExternalIdsToMap(externalIds, metadata));
                    });
        }
        List<Media> currentlyInMediaServer = mediaService.getMedia(apiKey, libraryId, externalIds);

        // - check if collection exists in media server (or else make)
        if (StringUtils.isBlank(collectionEntity.getExternalId())) {
            // not tracked/created yet
            // check if collection exists -
            // - if not, create
            // - else track (need to match on collection name...)
            CollectionEntity currentCollection = mediaService.getCollections(apiKey, libraryId, true)
                    .stream()
                    .filter(collection -> collection.getName().equals(collectionEntity.getName()))
                    .findFirst()
                    .orElse(null);
            if (currentCollection != null) {
                // combine since already exists
                collectionEntity.setExternalId(currentCollection.getExternalId());
            } else {
                // create
                currentCollection = new CollectionEntity();
                // TODO: update here to have correct info
                // TODO: check if need to include the media - then update metadata afterwards

//                mediaService.createOrUpdateCollection(apiKey, currentCollection, Collections.emptySet());
            }
        } else {
            // already in media server
        }

        // handle media that is acquired but not in collection
        // handle media not acquired

        return overseerMapper.map(entity.get(), Collection.class);
    }

    @Transactional
    public void processCheckCollections(Server server, Library library) {
        Map<String, String> options = Map.of("trackingType", CollectionTrackingType.UNTRACKED.name());
        List<Collection> collections = getCollections(server, library, options);
        if (!CollectionUtils.isEmpty(collections)) {
            WebApiService tmdbWebApiService = ApiUtils.retrieveWebApiService(TMDB, TmdbWebApiService.class, webApiServices, true);
            for (Collection collection : collections) {
                log.info("Processing collection {}", collection.getName());
                List<Collection> potentialMatches = tmdbWebApiService.searchCollections(collection.getName());
                if (!CollectionUtils.isEmpty(potentialMatches)) {
                    log.info("Found {} potential matches for {}", potentialMatches.size(), collection.getName());
                    for (Collection potentialMatch : potentialMatches) {
                        log.info(potentialMatch.getName());
                        for (Media media : potentialMatch.getMedia()) {
                            Optional<Metadata> name = media.getMetadata().stream().filter(part -> part.getName().equalsIgnoreCase(MetadataType.TITLE.name())).findFirst();
                            name.ifPresent(metadata -> log.info(metadata.getValue()));
                        }
                    }
                }
            }
        }
    }
    // endregion - Process -

    // region - CREATE -
    @Transactional
    public Collection addCollection(Server server, Collection collection, boolean process) {
        String collectionId = UUID.randomUUID().toString();
        CollectionEntity entity = overseerMapper.map(collection, CollectionEntity.class);
        entity.setId(collectionId);
        entity.setBuilders(null);

        try {
            entity = collectionRepository.save(entity);

            Set<CollectionBuilderEntity> builderEntities = overseerMapper.map(collection.getBuilders(), new TypeToken<Set<CollectionBuilderEntity>>() {
            }.getType());
            builderEntities.forEach(builder -> {
                builder.setId(UUID.randomUUID().toString());
                builder.setCollectionId(collectionId);
            });
            entity.setBuilders(new HashSet<>(collectionBuilderRepository.saveAll(builderEntities)));

            Set<SettingEntity> settingEntities = configureNewCollectionSettings(entity, overseerMapper.map(server, ServerEntity.class));
            entity.setSettings(new HashSet<>(settingRepository.saveAll(settingEntities)));
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

//        if (process) {
//            // [TEST]
//            log.debug("Collection processed");
//            // TODO: process collection
//        }

        return overseerMapper.map(entity, Collection.class);
    }

    // TODO: WIP 2025-06-24
//    public List<Collection> backupCollections(Server server, Library library, boolean resyncFromBackup) {
//        // This method is to check that all collections are backed up (via a JSON file) or resync the db from the backup file
//        if (resyncFromBackup) {
//
//        } else {
//
//        }
//
//        return null;
//    }
    // endregion - CREATE -

    // region - GET -
    public Collection getCollectionById(String id) {
        Optional<CollectionEntity> entity = collectionRepository.findById(id);

        if (entity.isEmpty()) {
            throw new OverseerNotFoundException(String.format("No collection found for provided criteria (collectionId: %s)", id));
        }

        return overseerMapper.map(entity.get(), Collection.class);
    }

    public List<Collection> getCollections(Server server, Library library, Map<String, String> options) {
        List<CollectionEntity> tracked = collectionRepository.findAllByLibraryId(library.getId());
        List<String> trackedExternalIds = tracked.stream().map(CollectionEntity::getExternalId).toList();

        ApiKeyEntity apiKey = overseerMapper.map(server.getApiKey(), ApiKeyEntity.class);
        String trackingTypeRequest = CollectionUtils.isEmpty(options) ? CollectionTrackingType.ALL_INFO.name() :
                options.getOrDefault("trackingType", CollectionTrackingType.ALL_INFO.name());
        // TODO: update here to see if tracked is the same as what's currently in mediaServer
        switch (CollectionTrackingType.findByName(trackingTypeRequest)) {
            case CollectionTrackingType.TRACKED_INFO: {
                // This will retrieve just the collections that are tracked via overseer (information only)
                if (CollectionUtils.isEmpty(tracked)) {
                    throw new OverseerNoContentException(String.format("No collections found for provided criteria (libraryId: %s)", library.getId()));
                } else {
                    return overseerMapper.map(tracked, new TypeToken<List<Collection>>() {
                    }.getType());
                }
            }
            case CollectionTrackingType.TRACKED: {
                // This will retrieve just the collections that are tracked via overseer (includes media)
                List<Collection> mediaServerCollections = getCollectionsFromMediaServer(apiKey, library, true);

                if (!CollectionUtils.isEmpty(trackedExternalIds)) {
                    mediaServerCollections.removeIf(collection -> !trackedExternalIds.contains(collection.getReferenceId()));
                }

                return mediaServerCollections;
            }
            case CollectionTrackingType.UNTRACKED_INFO: {
                // This will retrieve just the collections that are NOT tracked via overseer (information only)
                List<Collection> mediaServerCollections = getCollectionsFromMediaServer(apiKey, library, false);

                if (!CollectionUtils.isEmpty(trackedExternalIds)) {
                    mediaServerCollections.removeIf(collection -> trackedExternalIds.contains(collection.getReferenceId()));
                }

                return mediaServerCollections;
            }
            case CollectionTrackingType.UNTRACKED: {
                // This will retrieve just the collections that are NOT tracked via overseer (includes media)
                List<Collection> mediaServerCollections = getCollectionsFromMediaServer(apiKey, library, true);

                if (!CollectionUtils.isEmpty(trackedExternalIds)) {
                    mediaServerCollections.removeIf(collection -> StringUtils.isNotBlank(collection.getReferenceId()) || trackedExternalIds.contains(collection.getReferenceId()));
                }

                return mediaServerCollections;
            }
            case CollectionTrackingType.ALL: {
                // This will retrieve all collections (tracked and untracked) - including media
                List<Collection> mediaServerCollections = getCollectionsFromMediaServer(apiKey, library, true);

//                mediaServerCollections.forEach(collection -> {
//                    collection.setMedia(new HashSet<>(getCollectionMedia(collection.getReferenceId())));
//                });

                return mediaServerCollections;
            }
            case CollectionTrackingType.ALL_INFO: {
                // This will retrieve all collections (tracked and untracked) - information only - is also fallthrough to default
                return getCollectionsFromMediaServer(apiKey, library, false);
            }
            default: {
//                log.error("Collection Option [{}] is invalid", options);
                return Collections.emptyList();
            }
        }
    }
    // endregion - GET -

    // region - UPDATE -
    // TODO: update collection -> builder, attributes, etc.
    @Transactional
    public Collection updateCollection(Collection collection) {
        CollectionEntity entity = collectionRepository.findById(collection.getId()).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Collection found with id: [%s]", collection.getId()));
        }

        try {
            // Name
            entity.setName(collection.getName());

            // TODO: builders / attributes

            entity = collectionRepository.save(entity);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(entity, Collection.class);
    }

    @Transactional
    public Collection updateCollectionSettings(String collectionId, Set<Setting> collectionSettings) {
        CollectionEntity collection = collectionRepository.findById(collectionId).orElse(null);

        if (ObjectUtils.isEmpty(collection)) {
            throw new OverseerNotFoundException(String.format("No Collection found with id: [%s]", collectionId));
        }

        Set<SettingEntity> requested = overseerMapper.map(collectionSettings, new TypeToken<Set<SettingEntity>>() {
        }.getType());

        Set<SettingEntity> toSave = SettingUtils.syncSettings(collectionId, collection.getSettings(), requested, false);

        try {
            Set<SettingEntity> entities = new HashSet<>(settingRepository.saveAll(toSave));
            collection.setSettings(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(collection, Collection.class);
    }

    @Transactional
    public Collection updateCollectionActiveSetting(String collectionId, boolean active) {
        CollectionEntity collection = collectionRepository.findById(collectionId).orElse(null);

        if (ObjectUtils.isEmpty(collection)) {
            throw new OverseerNotFoundException(String.format("No Collection found with id: [%s]", collectionId));
        }

        SettingEntity activeSetting = collection.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().orElse(null);
        if (ObjectUtils.isEmpty(activeSetting)) {
            // new setting
            activeSetting = new SettingEntity();
            activeSetting.setId(UUID.randomUUID().toString());
            activeSetting.setReferenceId(collectionId);
            activeSetting.setName(Constants.ACTIVE_SETTING);
            activeSetting.setVal(String.valueOf(active));
            activeSetting.setType(SettingType.BOOLEAN);
        } else {
            activeSetting.setVal(String.valueOf(active));
        }

        try {
            settingRepository.save(activeSetting);
            collection = collectionRepository.findById(collectionId).orElse(null);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(collection, Collection.class);
    }

    @Transactional
    public Collection updateCollectionActions(String collectionId, Set<Action> collectionActions) {
        CollectionEntity collection = collectionRepository.findById(collectionId).orElse(null);

        if (ObjectUtils.isEmpty(collection)) {
            throw new OverseerNotFoundException(String.format("No Collection found with id: [%s]", collectionId));
        }

        Set<ActionEntity> requested = overseerMapper.map(collectionActions, new TypeToken<Set<ActionEntity>>() {
        }.getType());

        Set<ActionEntity> toSave = ActionUtils.syncActions(collectionId, collection.getActions(), requested, false);

        try {
            Set<ActionEntity> entities = new HashSet<>(actionRepository.saveAll(toSave));
            collection.setActions(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(collection, Collection.class);
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeCollection(String id) {
        try {
            collectionRepository.deleteById(id);

            // [TEST]
            log.debug("Collection removed");
        } catch (EmptyResultDataAccessException e) {
            throw new OverseerNotFoundException("No Collections matching provided id found");
        }
    }
    // endregion - DELETE -

    // region - Protected Methods -
    protected Set<SettingEntity> configureNewCollectionSettings(CollectionEntity collection, ServerEntity server) {
        LibraryEntity library = server.getLibraries().stream().filter(libraryEntity -> libraryEntity.getId().equals(collection.getLibraryId())).findFirst().orElse(null);
        Set<SettingEntity> collectionSettings = CollectionUtils.isEmpty(collection.getSettings()) ? new HashSet<>() : new HashSet<>(collection.getSettings());
        collectionSettings = SettingUtils.syncSettings(collection.getId(), collectionSettings, library.getSettings(), true);
        collectionSettings = SettingUtils.syncSettings(collection.getId(), collectionSettings, server.getSettings(), true);
        collectionSettings = SettingUtils.syncDefaultSettings(collection.getId(), collectionSettings, SettingLevel.LIBRARY);

        return collectionSettings;
    }

    protected List<Collection> getCollectionsFromMediaServer(ApiKeyEntity apiKey, Library library, boolean includeMedia) {
        MediaServerApiService service;
        switch (apiKey.getType()) {
            case PLEX ->
                    service = ApiUtils.retrieveMediaApiService(PLEX, PlexMediaServerApiService.class, mediaServerApiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }

        List<CollectionEntity> mediaServerCollections = service.getCollections(apiKey, library.getReferenceId(), includeMedia);
        if (CollectionUtils.isEmpty(mediaServerCollections)) {
            throw new OverseerNoContentException(String.format("No collections found for provided criteria (libraryId: %s)", library.getId()));
        } else {
            return overseerMapper.map(mediaServerCollections, new TypeToken<List<Collection>>() {
            }.getType());
        }
    }
    // endregion - Protected Methods -

    // [TEST]
    // process - getCollectionMedia
    public List<Media> getCollectionMedia(String collectionId) {
//        return tmdbWebApiService.getMediaFromCollection(collectionId);
        WebApiService tmdbWebApiService = ApiUtils.retrieveWebApiService(TMDB, TmdbWebApiService.class, webApiServices, true);
        return List.of(tmdbWebApiService.getSeries(collectionId));
    }

    public Media getMediaInfo(Server server, Library library, String mediaId) throws UnsupportedEncodingException {
        ApiKeyEntity apiKey = overseerMapper.map(server.getApiKey(), ApiKeyEntity.class);

        MediaServerApiService service;
        switch (apiKey.getType()) {
            case PLEX ->
                    service = ApiUtils.retrieveMediaApiService(PLEX, PlexMediaServerApiService.class, mediaServerApiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }

        Media media = service.getMedia(apiKey, library.getReferenceId(), mediaId);
//        media.getMetadata()
//                .stream()
//                .filter(metadata -> metadata.getName().equalsIgnoreCase(MetadataType.PATH.name()))
//                .findFirst()
//                .ifPresent(meta -> log.info(FilenameUtils.getFolderPath(meta.getValue())));
        return media;
    }
}
