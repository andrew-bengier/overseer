package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.Action;
import com.bnfd.overseer.model.api.Library;
import com.bnfd.overseer.model.api.Setting;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.model.persistence.*;
import com.bnfd.overseer.repository.ActionRepository;
import com.bnfd.overseer.repository.LibraryRepository;
import com.bnfd.overseer.repository.SettingRepository;
import com.bnfd.overseer.service.api.ApiService;
import com.bnfd.overseer.service.api.PlexApiService;
import com.bnfd.overseer.utils.ActionUtils;
import com.bnfd.overseer.utils.ApiUtils;
import com.bnfd.overseer.utils.Constants;
import com.bnfd.overseer.utils.SettingUtils;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.bnfd.overseer.model.constants.ApiKeyType.PLEX;

@Slf4j
@Service
public class LibraryService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final LibraryRepository libraryRepository;

    private final SettingRepository settingRepository;

    private final List<ApiService> apiServices;
    private final ActionRepository actionRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired

    public LibraryService(@Qualifier("overseer-mapper") ModelMapper overseerMapper,
                          LibraryRepository libraryRepository,
                          SettingRepository settingRepository,
                          List<ApiService> apiServices,
                          ActionRepository actionRepository) {
        this.overseerMapper = overseerMapper;
        this.libraryRepository = libraryRepository;
        this.settingRepository = settingRepository;
        this.apiServices = apiServices;
        this.actionRepository = actionRepository;
    }
    // endregion - Constructors -

    // TODO: process library
    // region - CREATE -
    @Transactional
    public List<Library> addLibraries(ServerEntity server) {
        Set<LibraryEntity> libraries = server.getLibraries();
        ApiKeyEntity apiKey = server.getApiKey();

        ApiService service;
        switch (apiKey.getName()) {
            case PLEX -> service = ApiUtils.retrieveService(PLEX, PlexApiService.class, apiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }

        try {
            List<LibraryEntity> entities = service.getLibraries(server);

            if (!CollectionUtils.isEmpty(libraries)) {
                log.info("Found {} libraries", libraries.size());
                return overseerMapper.map(libraries, new TypeToken<List<Library>>() {
                }.getType());
            } else {
                try {
                    entities.forEach(entity -> {
                        entity.setId(UUID.randomUUID().toString());
                        entity.setServerId(server.getId());
                        entity = libraryRepository.save(entity);

                        Set<SettingEntity> settingEntities = configureNewLibrarySettings(entity, server);
                        settingEntities = new HashSet<>(settingRepository.saveAll(settingEntities));
                        entity.setSettings(settingEntities);
                    });
                } catch (PersistenceException exception) {
                    throw new OverseerConflictException(exception.getMessage());
                }

                return overseerMapper.map(entities, new TypeToken<List<Library>>() {
                }.getType());
            }
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }
    // endregion - CREATE -

    // region - READ -
//     TODO: add param for how deep to retrieve (collections, media, etc.)
    public Library getLibraryById(String id) {
        LibraryEntity entity = libraryRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", id));
        }

        return overseerMapper.map(entity, Library.class);
    }

    public List<Library> getLibrariesByServerId(String serverId) {
        List<LibraryEntity> entities = libraryRepository.findAllByServerId(serverId);

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException(String.format("No libraries found for provided criteria (serverId: %s)", serverId));
        }

        return overseerMapper.map(entities, new TypeToken<List<Library>>() {
        }.getType());
    }
    // endregion - READ -

    // region - UPDATE -
    @Transactional
    public List<Library> resyncLibraries(ServerEntity server) {
        Set<LibraryEntity> currentLibraries = server.getLibraries();
        ApiKeyEntity apiKey = server.getApiKey();

        ApiService service;
        switch (apiKey.getName()) {
            case PLEX -> service = ApiUtils.retrieveService(PLEX, PlexApiService.class, apiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }

        try {
            List<LibraryEntity> libraries = service.getLibraries(server);
            for (LibraryEntity library : libraries) {
                LibraryEntity entity = currentLibraries.stream().filter(current -> current.getExternalId().equals(library.getExternalId())).findFirst().orElse(null);

                if (ObjectUtils.isEmpty(entity)) {
                    // new library
                    entity = new LibraryEntity();
                    entity.setId(UUID.randomUUID().toString());
                    entity.setServerId(server.getId());
                    entity.setExternalId(library.getExternalId());
                    entity.setType(library.getType());
                    entity.setName(library.getName());
                    entity = libraryRepository.save(entity);

                    Set<SettingEntity> settingEntities = configureNewLibrarySettings(entity, server);
                    settingEntities = new HashSet<>(settingRepository.saveAll(settingEntities));
                    entity.setSettings(settingEntities);
                } else {
                    entity.setName(library.getName());
                    entity = libraryRepository.save(entity);
                }
            }
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return getLibrariesByServerId(server.getId());
    }

    @Transactional
    public Library updateLibrary(Library library) {
        LibraryEntity entity = libraryRepository.findById(library.getId()).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", library.getId()));
        }

        try {
            entity.setName(library.getName());
            entity = libraryRepository.save(entity);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(entity, Library.class);
    }

    @Transactional
    public Library updateLibrarySettings(String libraryId, Set<Setting> librarySettings) {
        LibraryEntity library = libraryRepository.findById(libraryId).orElse(null);

        if (ObjectUtils.isEmpty(library)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", libraryId));
        }

        Set<SettingEntity> requested = overseerMapper.map(librarySettings, new TypeToken<Set<SettingEntity>>() {
        }.getType());

        Set<SettingEntity> toSave = SettingUtils.syncSettings(libraryId, library.getSettings(), requested, false);

        try {
            Set<SettingEntity> entities = new HashSet<>(settingRepository.saveAll(toSave));
            library.setSettings(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(library, Library.class);
    }

    @Transactional
    public Library updateLibraryActiveSetting(String libraryId, boolean active) {
        LibraryEntity library = libraryRepository.findById(libraryId).orElse(null);

        if (ObjectUtils.isEmpty(library)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", libraryId));
        }

        SettingEntity activeSetting = library.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().orElse(null);
        if (ObjectUtils.isEmpty(activeSetting)) {
            // new setting
            activeSetting = new SettingEntity();
            activeSetting.setId(UUID.randomUUID().toString());
            activeSetting.setReferenceId(libraryId);
            activeSetting.setName(Constants.ACTIVE_SETTING);
            activeSetting.setVal(String.valueOf(active));
            activeSetting.setType(SettingType.BOOLEAN);
        } else {
            activeSetting.setVal(String.valueOf(active));
        }

        try {
            settingRepository.save(activeSetting);
            library = libraryRepository.findById(libraryId).orElse(null);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(library, Library.class);
    }

    @Transactional
    public Library updateLibraryActions(String libraryId, Set<Action> libraryActions) {
        LibraryEntity library = libraryRepository.findById(libraryId).orElse(null);

        if (ObjectUtils.isEmpty(library)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", libraryId));
        }

        Set<ActionEntity> requested = overseerMapper.map(libraryActions, new TypeToken<Set<ActionEntity>>() {
        }.getType());

        Set<ActionEntity> toSave = ActionUtils.syncActions(libraryId, library.getActions(), requested, false);

        try {
            Set<ActionEntity> entities = new HashSet<>(actionRepository.saveAll(toSave));
            library.setActions(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(library, Library.class);
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeLibrary(String id) {
        try {
            libraryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", id));
        }
    }
    // endregion - DELETE -

    // region - Protected Methods -
    protected Set<SettingEntity> configureNewLibrarySettings(LibraryEntity library, ServerEntity server) {
        Set<SettingEntity> librarySettings = CollectionUtils.isEmpty(library.getSettings()) ? new HashSet<>() : new HashSet<>(library.getSettings());
        librarySettings = SettingUtils.syncSettings(library.getId(), librarySettings, server.getSettings(), true);
        librarySettings = SettingUtils.syncDefaultSettings(library.getId(), librarySettings, SettingLevel.LIBRARY);

        return librarySettings;
    }
    // endregion - Protected Methods -
}
