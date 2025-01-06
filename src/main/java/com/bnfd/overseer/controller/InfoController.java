package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.info.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.env.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("info")
public class InfoController {

    // region - Class Variables -
    private final Environment environment;

    private final GitInfo gitInfo;
    // endregion - Class Variables -

    @Autowired
    public InfoController(Environment environment, GitInfo gitInfo) {
        this.environment = environment;
        this.gitInfo = gitInfo;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiData getApiData() {
        log.info("Retrieving api data");

        return new ApiData(environment, gitInfo);
    }
}
