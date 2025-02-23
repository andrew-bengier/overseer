package com.bnfd.overseer.service;

import com.bnfd.overseer.config.*;
import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.persistence.servers.*;
import com.bnfd.overseer.repository.*;
import com.bnfd.overseer.utils.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import java.util.*;

@Slf4j
@Service
public class ServerService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final LibraryService libraryService;

    private final ServerRepository serverRepository;

    private final ServerSettingRepository serverSettingRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ServerService(@Qualifier("overseer-mapper") ModelMapper overseerMapper,
                         LibraryService libraryService,
                         ServerRepository serverRepository,
                         ServerSettingRepository serverSettingRepository
    ) {
        this.overseerMapper = overseerMapper;
        this.libraryService = libraryService;
        this.serverRepository = serverRepository;
        this.serverSettingRepository = serverSettingRepository;
    }
    // endregion - Constructors -

    // region - CREATE -
    @Transactional
    public Server addServer(Server server, boolean process) {
        ServerEntity entity = overseerMapper.map(server, ServerEntity.class);
        entity.setId(UUID.randomUUID().toString());
        entity = configureNewServer(entity);

        try {
            entity = serverRepository.save(entity);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        // [TEST]
        log.debug("Server added");

        if (process) {
            processServerById(entity.getId(), true);
        }

        return overseerMapper.map(entity, Server.class);
    }

    @Transactional
    public Server processServerById(String id, boolean addLibraries) {
        ServerEntity entity = serverRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", id));
        }

        List<Library> libraries;
        if (addLibraries) {
            libraries = libraryService.addLibraries(entity);
        } else {
            libraries = libraryService.getLibrariesByServerId(id);

            // TODO: call to process each library in server
        }

        Server server = overseerMapper.map(entity, Server.class);
        server.setLibraries(new HashSet<>(libraries));

        return server;
    }
    // endregion - CREATE -

    // region - READ -
    // TODO: add getters for libraries here
    public Server getServerById(String id) {
        ServerEntity entity = serverRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", id));
        }

        // [TEST]
        log.debug("Server: ", entity);

        return overseerMapper.map(entity, Server.class);
    }

    public List<Server> getAllServers() {
        List<ServerEntity> entities = serverRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No servers found");
        }

        // [TEST]
        log.debug("Servers: ", entities.size());

        return overseerMapper.map(entities, new TypeToken<List<Server>>() {
        }.getType());
    }
    // endregion - READ -

    // region - UPDATE -
    @Transactional
    public Server updateServer(Server server) {
        ServerEntity entity = serverRepository.findById(server.getId()).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", server.getId()));
        }

        entity = overseerMapper.map(server, ServerEntity.class);

        try {
            entity = serverRepository.save(entity);
            entity.setSettings(updateSettings(entity, entity.getSettings()));
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        // [TEST]
        log.debug("Server updated");

        return overseerMapper.map(entity, Server.class);
    }

    @Transactional
    public Server updateServerSettings(String serverId, Set<Setting> serverSettings) {
        ServerEntity entity = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        entity.setSettings(updateSettings(entity, overseerMapper.map(serverSettings, new TypeToken<Set<ServerSettingEntity>>() {
        }.getType())));

        // [TEST]
        log.debug("Server settings updated");

        return overseerMapper.map(entity, Server.class);
    }

    @Transactional
    public void updateServerActive(String serverId, boolean active) {
        ServerEntity entity = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        ServerSettingEntity activeSetting = null;
        if (!CollectionUtils.isEmpty(entity.getSettings())) {
            activeSetting = entity.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().orElse(null);
        }

        if (ObjectUtils.isEmpty(activeSetting)) {
            activeSetting = new ServerSettingEntity();
            activeSetting.setType(SettingType.BOOLEAN.name());
            activeSetting.setName(Constants.ACTIVE_SETTING);
            activeSetting.setVal(Boolean.FALSE.toString());
        }

        activeSetting.setVal(String.valueOf(active));

        serverSettingRepository.save(activeSetting);
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeServer(String id) {
        try {
            serverRepository.deleteById(id);

            // [TEST]
            log.debug("Server removed");
        } catch (EmptyResultDataAccessException e) {
            throw new OverseerNotFoundException("No Servers matching provided id found");
        }
    }
    // endregion - DELETE -

    // region - Protected Methods -
    protected ServerEntity configureNewServer(ServerEntity entity) {
        Set<ServerSettingEntity> settingEntities = CollectionUtils.isEmpty(entity.getSettings()) ? new HashSet<>() : entity.getSettings();
        Map<String, String> defaultSettings = DefaultSettings.getSettings();

        for (Map.Entry<String, String> defaultSetting : defaultSettings.entrySet()) {
            ServerSettingEntity serverSetting = settingEntities.stream().filter(setting -> defaultSetting.getKey().equalsIgnoreCase(setting.getName())).findFirst().orElse(null);

            if (ObjectUtils.isEmpty(serverSetting)) {
                if (defaultSetting.getKey().equalsIgnoreCase(Constants.ASSET_DIRECTORY_SETTING)) {
                    defaultSetting.setValue(defaultSetting.getValue() + FilenameUtils.sanitizeFilename(entity.getName()));
                }

                serverSetting = new ServerSettingEntity();
                serverSetting.setType(SettingUtils.getSettingType(defaultSetting.getValue()).name());
            } else {
                // TODO: validate setting done by user
            }

            serverSetting.setId(UUID.randomUUID().toString());
            serverSetting.setServer(entity.thinCopy());
            serverSetting.setName(defaultSetting.getKey());
            serverSetting.setVal(defaultSetting.getValue());

            settingEntities.add(serverSetting);
        }

        entity.setSettings(settingEntities);

        return entity;
    }

    protected Set<ServerSettingEntity> updateSettings(ServerEntity server, Set<ServerSettingEntity> settings) {
        List<ServerSettingEntity> serverSettings = CollectionUtils.isEmpty(server.getSettings()) ? new ArrayList<>() : new ArrayList<>(server.getSettings());

        for (ServerSettingEntity setting : settings) {
            ServerSettingEntity settingEntity = CollectionUtils.isEmpty(serverSettings) ? null : serverSettings.stream()
                    .filter(serverSetting -> serverSetting.getName().equalsIgnoreCase(setting.getName()))
                    .findFirst()
                    .orElse(null);

            if (ObjectUtils.isNotEmpty(settingEntity)) {
                overseerMapper.map(setting, settingEntity);
            } else {
                serverSettings.add(overseerMapper.map(setting, ServerSettingEntity.class));
            }
        }

        serverSettings.forEach(setting -> {
            setting.setId(UUID.randomUUID().toString());
            setting.setServer(server.thinCopy());
        });

        try {
            serverSettings = serverSettingRepository.saveAll(serverSettings);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return new HashSet<>(serverSettings);
    }
    // endregion - Protected Methods -
}
