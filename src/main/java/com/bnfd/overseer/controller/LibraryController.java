package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.Library;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Library Endpoints")
@RequestMapping("api/servers/{serverId}/libraries")
public class LibraryController {
    // region - Class Variables -
    private final ValidationService validationService;

    private final ServerService serverService;
    private final LibraryService libraryService;
    private final CollectionService collectionService;

    // [TEST]
    private final ImageAssetService imageAssetService;
    private final FileArchiveService fileArchiveService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LibraryController(ValidationService validationService,
                             ServerService serverService,
                             LibraryService libraryService,
                             CollectionService collectionService,
                             ImageAssetService imageAssetService,
                             FileArchiveService fileArchiveService) {
        this.validationService = validationService;
        this.serverService = serverService;
        this.libraryService = libraryService;
        this.collectionService = collectionService;
        this.imageAssetService = imageAssetService;
        this.fileArchiveService = fileArchiveService;
    }
    // endregion - Constructors -

    // region - POST -
    // TODO: "/{libraryId}/process"
    // endregion - POST -

    // region - GET -
    @GetMapping(value = "/{libraryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLibraryById(@PathVariable String serverId, @PathVariable String libraryId) throws Throwable {
        log.info("Retrieving library - id [{}]", libraryId);

        Library library = libraryService.getLibraryById(libraryId);

        return new ResponseEntity<>(library, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLibrariesByServerId(@PathVariable String serverId) throws Throwable {
        log.info("Retrieving libraries for server - id [{}]", serverId);

        List<Library> libraries = libraryService.getLibrariesByServerId(serverId);

        return new ResponseEntity<>(libraries, HttpStatus.OK);
    }
    // endregion - GET -

    // region - PUT -
    @PutMapping(value = "/{libraryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLibrary(@PathVariable String serverId, @PathVariable String libraryId, @RequestBody Library library) throws Throwable {
        log.info("Updating library - id [{}]", libraryId);

        validationService.validateLibrary(library, libraryId, false);

        return new ResponseEntity<>(libraryService.updateLibrary(library), HttpStatus.OK);
    }

    @PutMapping(value = "/{libraryId}/settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLibrarySettings(@PathVariable String serverId, @PathVariable String libraryId, @RequestBody Library library) throws Throwable {
        log.info("Updating library settings - id [{}]", libraryId);

        validationService.validateLibrary(library, libraryId, false);

        return new ResponseEntity<>(libraryService.updateLibrarySettings(libraryId, library.getSettings()), HttpStatus.OK);
    }

    @PutMapping(value = "/{libraryId}/actions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLibraryActions(@PathVariable String serverId, @PathVariable String libraryId, @RequestBody Library library) throws Throwable {
        log.info("Updating library actions - id [{}]", libraryId);

        validationService.validateLibrary(library, libraryId, false);

        return new ResponseEntity<>(libraryService.updateLibraryActions(libraryId, library.getActions()), HttpStatus.OK);
    }
    // endregion - PUT -

    // region - PATCH -
    @PatchMapping(value = "/{libraryId}/active", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLibraryActive(@PathVariable String serverId, @PathVariable String libraryId, @RequestParam boolean active) throws Throwable {
        log.info("Updating library active [{}] - id [{}]", active, libraryId);

        return new ResponseEntity<>(libraryService.updateLibraryActiveSetting(libraryId, active), HttpStatus.OK);
    }
    // endregion - PATCH -

    // region - DELETE -
    @DeleteMapping(value = "/{libraryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLibrary(@PathVariable String serverId, @PathVariable String libraryId) {
        log.info("Deleting library - id [{}]", libraryId);

        libraryService.removeLibrary(libraryId);

        return ResponseEntity.ok().build();
    }
    // endregion - DELETE -

    // [TEST]
    @PostMapping(value = "/{libraryId}/test/processCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processCheck(@PathVariable String serverId, @PathVariable String libraryId) throws Throwable {
        log.info("Processing check collections for - id [{}]", libraryId);

        Server server = serverService.getServerById(serverId);
        Library library = libraryService.getLibraryById(libraryId);

        collectionService.processCheckCollections(server, library);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{libraryId}/test/assetsArchive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processArchive(@PathVariable String serverId, @PathVariable String libraryId, @RequestParam(defaultValue = "false") boolean centerOverlay) throws Throwable {
        log.info("Processing archive for - id [{}]", libraryId);

        imageAssetService.testOverlay(centerOverlay);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{libraryId}/test/getMediaInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMediaInfo(@PathVariable String serverId, @PathVariable String libraryId, @RequestParam String mediaId) throws UnsupportedEncodingException {
        Server server = serverService.getServerById(serverId);
        Library library = libraryService.getLibraryById(libraryId);

        return new ResponseEntity<>(collectionService.getMediaInfo(server, library, mediaId), HttpStatus.OK);
    }

    @GetMapping(value = "/{libraryId}/test/checkArchiveEligible", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkArchiveEligible(@PathVariable String serverId, @PathVariable String libraryId) throws UnsupportedEncodingException {
        Server server = serverService.getServerById(serverId);
        Library library = libraryService.getLibraryById(libraryId);

        return new ResponseEntity<>(libraryService.checkArchiveEligible(server, library), HttpStatus.OK);
    }

    @PostMapping(value = "/{libraryId}/test/archive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> archive(@PathVariable String serverId, @PathVariable String libraryId, @RequestParam(defaultValue = "false") boolean centerOverlay) throws Throwable {
        log.info("Processing archive for - id [{}]", libraryId);

        fileArchiveService.archiveMedia();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
