package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.info.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.info.*;
import org.springframework.core.env.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("info")
@CrossOrigin(origins = "http://localhost:3000")
public class InfoController extends ApiController {

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
