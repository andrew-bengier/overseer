package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.service.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/apikeys")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiKeyController {
    // region - Class Variables -
    private final ValidationService validationService;

    private final ApiKeyService apiKeyService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ApiKeyController(ValidationService validationService, ApiKeyService apiKeyService) {
        this.validationService = validationService;
        this.apiKeyService = apiKeyService;
    }
    // endregion - Constructors -

    // region - POST -
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addApiKey(@RequestBody ApiKey apikey) throws Throwable {
        log.info("Adding api key");

        validationService.validateApiKey(apikey, null, true);

        return new ResponseEntity<>(apiKeyService.addApiKey(apikey), HttpStatus.OK);
    }
    // endregion - POST -

    // region - GET -
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getApiKeyById(@PathVariable String id) {
        log.info(String.format("Retrieving api key - id [%s]", id));

        return new ResponseEntity<>(apiKeyService.getApiKeyById(id), HttpStatus.OK);
    }

    // TODO: convert here to pageable request (including pageable queries to db)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getApiKeys() {
        log.info("Retrieving api keys");

        return new ResponseEntity<>(apiKeyService.getAllApiKeys(), HttpStatus.OK);
    }

    // TODO: convert here to pageable request (including pageable queries to db)
    @GetMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchApiKeys(@RequestParam Map<String, String> requestParams) {
        log.info("Searching api keys");

        // TODO: change this validation (temporary)
        if (StringUtils.isBlank(requestParams.get("name"))) {
            throw new OverseerBadRequestException(List.of("Missing search param - name"));
        }

        return new ResponseEntity<>(apiKeyService.getAllApiKeysByName(requestParams.get("name")), HttpStatus.OK);
    }
    // endregion - GET -

    // region - PUT -
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateApiKey(@PathVariable String id, @RequestBody ApiKey apiKey) throws Throwable {
        log.info(String.format("Updating api key - id [%s]", id));

        validationService.validateApiKey(apiKey, id, false);

        return new ResponseEntity<>(apiKeyService.updateApiKey(apiKey), HttpStatus.OK);
    }
    // endregion - PUT -

    // region - DELETE -
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteApiKey(@PathVariable String id) {
        log.info(String.format("Deleting api key - id [%s]", id));

        apiKeyService.removeApiKey(id);

        return ResponseEntity.ok().build();
    }
    // endregion - DELETE -
}
