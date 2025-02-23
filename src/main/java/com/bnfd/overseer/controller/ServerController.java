package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.service.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
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
    public ResponseEntity<?> addServer(@RequestParam(required = false, defaultValue = "false") boolean process, @RequestBody Server server) throws Throwable {
        log.info(String.format("Adding server - processing: %s ", process));

        validationService.validateServer(server, null, true);

        try {
            return new ResponseEntity<>(serverService.addServer(server, process), HttpStatus.OK);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }

    @PostMapping(value = "/{id}/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processServerById(@PathVariable String id, @RequestParam(required = false, defaultValue = "false") boolean addLibraries) throws Throwable {
        log.info(String.format("Processing server (adding libraries: %s) - id [%s]", addLibraries, id));

        try {
            return new ResponseEntity<>(serverService.processServerById(id, addLibraries), HttpStatus.OK);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }
    // endregion - POST -

    // region - GET -
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServers() throws Throwable {
        log.info("Retrieving servers");

        return new ResponseEntity<>(serverService.getAllServers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServerById(@PathVariable String id) throws Throwable {
        log.info(String.format("Retrieving server - id [%s]", id));

        return new ResponseEntity<>(serverService.getServerById(id), HttpStatus.OK);
    }
    // endregion - GET -

    // region - PUT -
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServer(@PathVariable String id, @RequestBody Server server) throws Throwable {
        log.info(String.format("Updating server - id [%s]", id));

        validationService.validateServer(server, id, false);

        try {
            return new ResponseEntity<>(serverService.updateServer(server), HttpStatus.OK);
        } catch (PersistenceException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }
    }
    // endregion - PUT -

    // region - PATCH -
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServerActive(@PathVariable String id, @RequestParam boolean active) throws Throwable {
        log.info(String.format("Updating server active [%s] - id [%s]", active, id));

        serverService.updateServerActive(id, active);

        return ResponseEntity.ok().build();
    }
    // endregion - PATCH -

    // region - DELETE -
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteApiKey(@PathVariable String id) {
        log.info(String.format("Deleting server - id [%s]", id));

        serverService.removeServer(id);

        return ResponseEntity.ok().build();
    }
    // endregion - DELETE -
}
