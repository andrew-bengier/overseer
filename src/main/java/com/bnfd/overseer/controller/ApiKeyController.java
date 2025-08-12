package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.service.ApiKeyService;
import com.bnfd.overseer.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "ApiKey Endpoints")
@RequestMapping("api/apikeys")
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
    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiKey.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"message\": \"Bad Request\", \"errors\": [\"Missing api key\"], \"status\": \"BAD_REQUEST\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"message\": \"Conflict\", \"status\": \"CONFLICT\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"message\": \"Unprocessable Entity\", \"errors\": [\"Invalid api key non-null id\"], \"status\": \"UNPROCESSABLE_ENTITY\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"message\": \"Internal Server Error\", \"status\": \"INTERNAL_SERVER_ERROR\"}"
                                            )
                                    }
                            )
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ApiKey",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiKey.class)
                    )
            ),
            description = "Add ApiKey"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addApiKey(@RequestBody ApiKey apikey) throws Throwable {
        log.info("Adding api key");

        validationService.validateApiKey(apikey, null, true);

        return new ResponseEntity<>(apiKeyService.addApiKey(apikey), HttpStatus.OK);
    }

    @PostMapping(value = "/requirements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppRequirements() throws Throwable {
        log.info("Retrieving app requirements: api keys");

        List<ApiKey> apiKeys = apiKeyService.getAllApiKeys();
        apiKeys = apiKeys.stream().filter(apiKey -> ValidationService.getRequiredApiKeys().contains(apiKey.getType())).toList();

        return new ResponseEntity<>(apiKeys, HttpStatus.OK);
    }
    // endregion - POST -

    // region - GET -
    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiKey.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            required = true,
                            name = "id",
                            description = "ApiKey Id",
                            schema = @Schema(implementation = String.class)
                    )
            },
            description = "Get ApiKey By Id"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getApiKeyById(@PathVariable(name = "id") String id) {
        log.info(String.format("Retrieving api key - id [%s]", id));

        return new ResponseEntity<>(apiKeyService.getApiKeyById(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getApiKeys(@RequestParam(required = false) Map<String, String> requestParams) throws Throwable {
        log.info("Searching api keys");

        if (MapUtils.isNotEmpty(requestParams)) {
            validationService.validateApiKeySearchParams(requestParams);

            ApiKeyType apiKeyType = ApiKeyType.valueOf(requestParams.get("type").toUpperCase());
            return new ResponseEntity<>(apiKeyService.getAllApiKeysByType(apiKeyType), HttpStatus.OK);
        } else {
            log.info("Retrieving all api keys");
            return new ResponseEntity<>(apiKeyService.getAllApiKeys(), HttpStatus.OK);
        }
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
