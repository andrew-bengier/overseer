package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.Action;
import com.bnfd.overseer.model.api.Library;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.model.api.Setting;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.model.persistence.ActionEntity;
import com.bnfd.overseer.model.persistence.ServerEntity;
import com.bnfd.overseer.model.persistence.SettingEntity;
import com.bnfd.overseer.repository.ActionRepository;
import com.bnfd.overseer.repository.ServerRepository;
import com.bnfd.overseer.repository.SettingRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ServerService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final ServerRepository serverRepository;

    private final SettingRepository settingRepository;

    private final ActionRepository actionRepository;

    private final LibraryService libraryService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ServerService(@Qualifier("overseer-mapper") ModelMapper overseerMapper,
                         ServerRepository serverRepository,
                         SettingRepository settingRepository,
                         ActionRepository actionRepository,
                         LibraryService libraryService
    ) {
        this.overseerMapper = overseerMapper;
        this.serverRepository = serverRepository;
        this.settingRepository = settingRepository;
        this.actionRepository = actionRepository;
        this.libraryService = libraryService;
    }
    // endregion - Constructors -

    // TODO: process server
//    @Transactional
//    public Server processServerById(String id, boolean addLibraries) {
//        ServerEntity entity = serverRepository.findById(id).orElse(null);
//
//        if (ObjectUtils.isEmpty(entity)) {
//            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", id));
//        }
//
//        Set<ServerSettingEntity> serverSettings = serverSettingRepository.findAllByServerId(entity.getId());
//        serverSettings.forEach(serverSetting -> {
//            serverSetting.setServer(entity.thinCopy());
//        });
//        entity.setSettings(serverSettings);
//
//        List<Library> libraries;
//        if (addLibraries) {
//            libraries = libraryService.addLibraries(entity);
//        } else {
//            libraries = libraryService.getLibrariesByServerId(id, false);
//
//            // TODO: call to process each library in server
//        }
//
//        Server server = overseerMapper.map(entity, Server.class);
//        server.setLibraries(new HashSet<>(libraries));
//
//        return server;
//    }

    // region - CREATE -
    @Transactional
    public Server addServer(Server server, boolean includeLibraries) {
        ServerEntity entity = overseerMapper.map(server, ServerEntity.class);
        entity.setId(UUID.randomUUID().toString());

        try {
            entity = serverRepository.save(entity);

            Set<SettingEntity> settingEntities = configureNewServerSettings(entity);
            settingEntities = new HashSet<>(settingRepository.saveAll(settingEntities));
            entity.setSettings(settingEntities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        server = overseerMapper.map(entity, Server.class);

        if (includeLibraries) {
            List<Library> libraries = libraryService.addLibraries(entity);
            server.setLibraries(new HashSet<>(libraries));
        }

        return server;
    }
    // endregion - CREATE -

    // region - READ -
    public Server getServerById(String id) {
        ServerEntity entity = serverRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", id));
        }

        return overseerMapper.map(entity, Server.class);
    }

    public List<Server> getAllServers() {
        List<ServerEntity> entities = serverRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No servers found");
        }

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

        try {
            entity.setName(server.getName());
            entity = serverRepository.save(entity);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(entity, Server.class);
    }

    @Transactional
    public Server updateServerSettings(String serverId, Set<Setting> serverSettings) {
        ServerEntity server = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(server)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        Set<SettingEntity> requested = overseerMapper.map(serverSettings, new TypeToken<Set<SettingEntity>>() {
        }.getType());

        Set<SettingEntity> toSave = SettingUtils.syncSettings(serverId, server.getSettings(), requested, false);

        try {
            Set<SettingEntity> entities = new HashSet<>(settingRepository.saveAll(toSave));
            server.setSettings(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(server, Server.class);
    }

    @Transactional
    public Server updateServerActiveSetting(String serverId, boolean active) {
        ServerEntity server = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(server)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        SettingEntity activeSetting = server.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().orElse(null);
        if (ObjectUtils.isEmpty(activeSetting)) {
            // new setting
            activeSetting = new SettingEntity();
            activeSetting.setId(UUID.randomUUID().toString());
            activeSetting.setReferenceId(serverId);
            activeSetting.setName(Constants.ACTIVE_SETTING);
            activeSetting.setVal(String.valueOf(active));
            activeSetting.setType(SettingType.BOOLEAN);
        } else {
            activeSetting.setVal(String.valueOf(active));
        }

        try {
            settingRepository.save(activeSetting);
            server = serverRepository.findById(serverId).orElse(null);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(server, Server.class);
    }

    @Transactional
    public Server updateServerActions(String serverId, Set<Action> serverActions) {
        ServerEntity server = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(server)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        Set<ActionEntity> requested = overseerMapper.map(serverActions, new TypeToken<Set<ActionEntity>>() {
        }.getType());

        Set<ActionEntity> toSave = ActionUtils.syncActions(serverId, server.getActions(), requested, false);

        try {
            Set<ActionEntity> entities = new HashSet<>(actionRepository.saveAll(toSave));
            server.setActions(entities);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        return overseerMapper.map(server, Server.class);
    }

    @Transactional
    public Server resyncServer(String serverId) {
        ServerEntity entity = serverRepository.findById(serverId).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", serverId));
        }

        Server server = overseerMapper.map(entity, Server.class);
        server.setLibraries(new HashSet<>(libraryService.resyncLibraries(entity)));

        return server;
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeServer(String id) {
        try {
            serverRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new OverseerNotFoundException(String.format("No Server found with id: [%s]", id));
        }
    }
    // endregion - DELETE -

    // region - Protected Methods -
    protected Set<SettingEntity> configureNewServerSettings(ServerEntity server) {
        Set<SettingEntity> serverSettings = CollectionUtils.isEmpty(server.getSettings()) ? new HashSet<>() : new HashSet<>(server.getSettings());
        serverSettings = SettingUtils.syncDefaultSettings(server.getId(), serverSettings, SettingLevel.SERVER);

        return serverSettings;
    }
    // endregion - Protected Methods -
}
