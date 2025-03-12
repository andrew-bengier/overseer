package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.service.ServerService;
import com.bnfd.overseer.service.ValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Server Endpoints")
@RequestMapping("api/servers")
@CrossOrigin(origins = "http://localhost:3000")
public class ServerController {
    // region - Class Variables -
    private final ValidationService validationService;

    private final ServerService serverService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ServerController(ValidationService validationService, ServerService serverService) {
        this.validationService = validationService;
        this.serverService = serverService;
    }
    // endregion - Constructors -

    // region - POST -
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addServer(@RequestParam(required = false, defaultValue = "false") boolean includeLibraries, @RequestBody Server server) throws Throwable {
        log.info("Adding server - including libraries {}", includeLibraries);

        validationService.validateServer(server, null, true);

        try {
            return new ResponseEntity<>(serverService.addServer(server, includeLibraries), HttpStatus.OK);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }

    // TODO: "/{serverId}/process"
//    @PostMapping(value = "/{serverId}/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> processServerById(@PathVariable String serverId, @RequestParam(required = false, defaultValue = "false") boolean addLibraries) throws Throwable {
//        log.info(String.format("Processing server (adding libraries: %s) - id [%s]", addLibraries, serverId));
//
//        try {
//            return new ResponseEntity<>(serverService.processServerById(serverId, addLibraries), HttpStatus.OK);
//        } catch (PersistenceException exception) {
//            throw new OverseerConflictException(exception.getMessage());
//        }
//    }
    // endregion - POST -

    // region - GET -
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServers() throws Throwable {
        log.info("Retrieving servers");

        return new ResponseEntity<>(serverService.getAllServers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerById(@PathVariable String serverId) throws Throwable {
        log.info("Retrieving server - id [{}]", serverId);

        return new ResponseEntity<>(serverService.getServerById(serverId), HttpStatus.OK);
    }
    // endregion - GET -

    // region - PUT -
    @PutMapping(value = "/{serverId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServer(@PathVariable String serverId, @RequestBody Server server) throws Throwable {
        log.info("Updating server - id [{}]", serverId);

        validationService.validateServer(server, serverId, false);

        return new ResponseEntity<>(serverService.updateServer(server), HttpStatus.OK);
    }

    @PutMapping(value = "/{serverId}/settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServerSettings(@PathVariable String serverId, @RequestBody Server server) throws Throwable {
        log.info("Updating server settings - id [{}]", serverId);

        validationService.validateServer(server, serverId, false);

        return new ResponseEntity<>(serverService.updateServerSettings(serverId, server.getSettings()), HttpStatus.OK);
    }

    @PutMapping(value = "/{serverId}/actions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServerActions(@PathVariable String serverId, @RequestBody Server server) throws Throwable {
        log.info("Updating server actions - id [{}]", serverId);

        validationService.validateServer(server, serverId, false);

        return new ResponseEntity<>(serverService.updateServerActions(serverId, server.getActions()), HttpStatus.OK);
    }

    @PutMapping(value = "/{serverId}/resync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resyncServer(@PathVariable String serverId) throws Throwable {
        log.info("Resyncing server - id [{}]", serverId);

        return new ResponseEntity<>(serverService.resyncServer(serverId), HttpStatus.OK);
    }
    // endregion - PUT -

    // region - PATCH -
    @PatchMapping(value = "/{serverId}/active", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServerActive(@PathVariable String serverId, @RequestParam boolean active) throws Throwable {
        log.info("Updating server active [{}] - id [{}]", active, serverId);

        return new ResponseEntity<>(serverService.updateServerActiveSetting(serverId, active), HttpStatus.OK);
    }
    // endregion - PATCH -

    // region - DELETE -
    @DeleteMapping(value = "/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteServer(@PathVariable String serverId) {
        log.info("Deleting server - id [{}]", serverId);

        serverService.removeServer(serverId);

        return ResponseEntity.ok().build();
    }
    // endregion - DELETE -
}
