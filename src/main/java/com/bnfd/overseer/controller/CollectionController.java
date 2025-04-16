package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.Collection;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.service.CollectionService;
import com.bnfd.overseer.service.LibraryService;
import com.bnfd.overseer.service.ServerService;
import com.bnfd.overseer.service.ValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Collection Endpoints")
@RequestMapping("api/servers/{serverId}/libraries/{libraryId}/collections")
public class CollectionController {
    // region - Class Variables -
    private final ValidationService validationService;

    private final ServerService serverService;

    private final LibraryService libraryService;

    private final CollectionService collectionService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public CollectionController(ValidationService validationService, ServerService serverService, LibraryService libraryService, CollectionService collectionService) {
        this.validationService = validationService;
        this.serverService = serverService;
        this.libraryService = libraryService;
        this.collectionService = collectionService;
    }
    // endregion - Constructors -

    // region - POST -
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection> addCollection(@PathVariable("serverId") String serverId, @PathVariable String libraryId, @RequestParam(required = false, defaultValue = "false") boolean process, @RequestBody Collection collection) throws Throwable {
        log.info("Adding collection - processing: {} ", process);

        validationService.validateCollection(collection, null, true);

        Server server = serverService.getServerById(serverId);
        return new ResponseEntity<>(collectionService.addCollection(server, collection, process), HttpStatus.OK);
    }
    // endregion - POST -

    // region - GET -
    @GetMapping(value = "/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCollectionById(@PathVariable String serverId, @PathVariable String libraryId, @PathVariable String collectionId) throws Throwable {
        log.info("Retrieving collection - id [{}]", collectionId);

        return new ResponseEntity<>(collectionService.getCollectionById(collectionId), HttpStatus.OK);
    }
    // endregion - GET -

    // region - PUT -
    @PutMapping(value = "/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processCollection(@PathVariable String serverId, @PathVariable String libraryId, @PathVariable String collectionId) throws Throwable {
        log.info("Processing collection - id [{}]", collectionId);


        Server server = serverService.getServerById(serverId);
        return new ResponseEntity<>(collectionService.processCollectionById(collectionId), HttpStatus.OK);
    }
    // endregion - PUT -

    // region - PATCH -
    // endregion - PATCH -

    // region - DELETE -
    // endregion - DELETE -

    // [TEST]
    @GetMapping(value = "/{collectionId}/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testCollection(@PathVariable String serverId, @PathVariable String libraryId, @PathVariable String collectionId) throws Throwable {
        log.info("Testing collection - id [{}]", collectionId);

        Server server = serverService.getServerById(serverId);
//        return new ResponseEntity<>(libraryService.getMediaContainer(server), HttpStatus.OK);
        return new ResponseEntity<>(collectionService.getCollectionMedia(collectionId), HttpStatus.OK);
    }
}
