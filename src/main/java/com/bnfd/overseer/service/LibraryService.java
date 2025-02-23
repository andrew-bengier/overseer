package com.bnfd.overseer.service;

import com.bnfd.overseer.config.*;
import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.persistence.apikeys.*;
import com.bnfd.overseer.model.persistence.libraries.*;
import com.bnfd.overseer.model.persistence.servers.*;
import com.bnfd.overseer.repository.*;
import com.bnfd.overseer.service.api.*;
import com.bnfd.overseer.utils.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import java.util.*;

import static com.bnfd.overseer.model.constants.ApiKeyType.*;

@Slf4j
@Service
public class LibraryService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final LibraryRepository libraryRepository;

    private final LibrarySettingRepository librarySettingRepository;

    private final List<ApiService> apiServices;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired

    public LibraryService(@Qualifier("overseer-mapper") ModelMapper overseerMapper,
                          LibraryRepository libraryRepository,
                          LibrarySettingRepository librarySettingRepository,
                          List<ApiService> apiServices
    ) {
        this.overseerMapper = overseerMapper;
        this.libraryRepository = libraryRepository;
        this.librarySettingRepository = librarySettingRepository;
        this.apiServices = apiServices;
    }
    // endregion - Constructors -

    // region - CREATE -
    // create as new library in existing server
    // create from process
    @Transactional
    public List<Library> addLibraries(ServerEntity server) {
        ApiKeyEntity apiKey = server.getApiKey();

        ApiService service;
        switch (ApiKeyType.findByName(apiKey.getName())) {
            case PLEX -> service = ApiUtils.retrieveService(PLEX, PlexApiService.class, apiServices, true);
            default -> throw new OverseerException("Error - service for server type not currently supported");
        }

        try {
            List<LibraryEntity> entities = service.getLibraries(server);
            try {
                entities.stream()
                        .map(entity -> {
                            entity.setId(UUID.randomUUID().toString());
                            entity.setServer(server.thinCopy());
                            entity.setSettings(configureNewLibrarySettings(entity, server));
                            entity.fromPersistence(libraryRepository.save(entity));
                            return entity;
                        })
                        .toList();
            } catch (PersistenceException exception) {
                throw new OverseerConflictException(exception.getMessage());
            }

            // [TEST]
            log.debug("Libraries added for server");

            return overseerMapper.map(entities, new TypeToken<List<Library>>() {
            }.getType());
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }
    // endregion - CREATE -

    // region - READ -
    // TODO: add param for how deep to retrieve (collections, media, etc.)
    public Library getLibraryById(String id) {
        LibraryEntity entity = libraryRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Library found with id: [%s]", id));
        }

        // [TEST]
        log.debug("Library: ", entity);

        return overseerMapper.map(entity, Library.class);
    }

    public List<Library> getLibrariesByServerId(String id) {
        List<LibraryEntity> entities = libraryRepository.findAllByServerId(id);

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException(String.format("No libraries found for provided criteria (serverId: %s)", id));
        }

        // [TEST]
        log.debug(String.format("Libraries for server %s: ", id), entities.size());

        return overseerMapper.map(entities, new TypeToken<List<Library>>() {
        }.getType());
    }
    // endregion - READ -

    // region - UPDATE -
    // endregion - UPDATE -

    // region - DELETE -
    // endregion - DELETE -

    // region - Protected Methods -
    protected Set<LibrarySettingEntity> configureNewLibrarySettings(LibraryEntity library, ServerEntity server) {
        Set<LibrarySettingEntity> librarySettings = CollectionUtils.isEmpty(library.getSettings()) ? new HashSet<>() : library.getSettings();
        Map<String, String> defaultSettings = DefaultSettings.getSettings();

        for (Map.Entry<String, String> defaultSetting : defaultSettings.entrySet()) {
            LibrarySettingEntity librarySetting = librarySettings.stream().filter(setting -> defaultSetting.getKey().equalsIgnoreCase(setting.getName())).findFirst().orElse(null);

            if (ObjectUtils.isEmpty(librarySetting)) {
                if (defaultSetting.getKey().equalsIgnoreCase(Constants.ASSET_DIRECTORY_SETTING)) {
                    ServerSettingEntity serverAssetDirectory = server.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ASSET_DIRECTORY_SETTING)).findFirst().orElse(null);

                    defaultSetting.setValue(serverAssetDirectory.getVal() + "/" + FilenameUtils.sanitizeFilename(library.getName()));
                }

                librarySetting = new LibrarySettingEntity();
                librarySetting.setType(SettingUtils.getSettingType(defaultSetting.getValue()).name());
            } else {
                // TODO: validate setting done by user
            }

            librarySetting.setId(UUID.randomUUID().toString());
            librarySetting.setLibrary(library.thinCopy());
            librarySetting.setName(defaultSetting.getKey());
            librarySetting.setVal(defaultSetting.getValue());

            librarySettings.add(librarySetting);
        }

        return librarySettings;
    }

    protected Set<LibrarySettingEntity> updateSettings(LibraryEntity library, Set<LibrarySettingEntity> settings) {
        List<LibrarySettingEntity> librarySettings = new ArrayList<>(library.getSettings());

        for (LibrarySettingEntity setting : settings) {
            LibrarySettingEntity settingEntity = librarySettings.stream()
                    .filter(set -> set.getName().equalsIgnoreCase(setting.getName()))
                    .findFirst()
                    .orElse(null);

            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(settingEntity)) {
                overseerMapper.map(setting, settingEntity);
            } else {
                librarySettings.add(overseerMapper.map(setting, LibrarySettingEntity.class));
            }
        }

        librarySettings.forEach(setting -> setting.setLibrary(library.thinCopy()));

        try {
            librarySettings = librarySettingRepository.saveAll(librarySettings);
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return new HashSet<>(librarySettings);
    }
    // endregion - Protected Methods -
}
