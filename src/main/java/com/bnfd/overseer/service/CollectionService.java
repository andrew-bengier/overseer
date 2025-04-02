package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.model.persistence.*;
import com.bnfd.overseer.repository.*;
import com.bnfd.overseer.service.api.web.TmdbWebApiService;
import com.bnfd.overseer.utils.ActionUtils;
import com.bnfd.overseer.utils.Constants;
import com.bnfd.overseer.utils.SettingUtils;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    // [TEST]
    private final TmdbWebApiService tmdbWebApiService;
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
            TmdbWebApiService tmdbWebApiService) {
        this.overseerMapper = overseerMapper;
        this.collectionRepository = collectionRepository;
        this.collectionBuilderRepository = collectionBuilderRepository;
        this.settingRepository = settingRepository;
        this.actionRepository = actionRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.tmdbWebApiService = tmdbWebApiService;
    }
    // endregion - Constructors -

    @Transactional
    public Collection processCollectionById(String collectionId) {
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

        collectionEntity.getBuilders().forEach(collectionBuilder -> {
            log.debug("Processing builder {} {} - {}: {}", collectionBuilder.getType(), collectionBuilder.getCategory(), collectionBuilder.getName(), String.join(", ", collectionBuilder.getBuilderAttributes()));
        });

        return overseerMapper.map(entity.get(), Collection.class);
    }

    // [TEST]
    // process - getCollectionMedia
    public List<Media> getCollectionMedia(String collectionId) {
//        return tmdbWebApiService.getMediaFromCollection(collectionId);
        return List.of(tmdbWebApiService.getSeries(collectionId));
    }

    // region - CREATE -
    @Transactional
    public Collection addCollection(Server server, Collection collection, boolean process) {
        CollectionEntity entity = overseerMapper.map(collection, CollectionEntity.class);
        entity.setId(UUID.randomUUID().toString());
        entity.setBuilders(null);

        try {
            entity = collectionRepository.save(entity);

            Set<CollectionBuilderEntity> builderEntities = overseerMapper.map(collection.getBuilders(), new TypeToken<Set<CollectionBuilderEntity>>() {
            }.getType());
            builderEntities.forEach(builder -> {
                builder.setId(UUID.randomUUID().toString());
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
    // endregion - CREATE -

    // region - GET -
    public Collection getCollectionById(String id) {
        Optional<CollectionEntity> entity = collectionRepository.findById(id);

        if (entity.isEmpty()) {
            throw new OverseerNotFoundException(String.format("No collection found for provided criteria (collectionId: %s)", id));
        }

        return overseerMapper.map(entity.get(), Collection.class);
    }

    public List<Collection> getCollectionsByLibraryId(String libraryId) {
        List<CollectionEntity> entities = collectionRepository.findAllByLibraryId(libraryId);

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException(String.format("No collections found for provided criteria (libraryId: %s)", libraryId));
        }

        return overseerMapper.map(entities, new TypeToken<List<Collection>>() {
        }.getType());
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
    // endregion - Protected Methods -
}
