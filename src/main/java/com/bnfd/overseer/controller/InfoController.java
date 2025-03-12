package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.info.ApiData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Info Endpoints")
@RequestMapping("api/info")
@CrossOrigin(origins = "http://localhost:3000")
public class InfoController {

    // region - Class Variables -
    private final Environment environment;

    private final BuildProperties buildProperties;
    // endregion - Class Variables -

    @Autowired
    public InfoController(Environment environment, BuildProperties buildProperties) {
        this.environment = environment;
        this.buildProperties = buildProperties;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiData getApiData() {
        log.info("Retrieving api data");

        return new ApiData(environment, buildProperties);
    }
}
