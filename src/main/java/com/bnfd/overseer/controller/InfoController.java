package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.info.ApiData;
import com.bnfd.overseer.service.LogService;
import com.bnfd.overseer.service.api.GitApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Info Endpoints")
@RequestMapping("api/info")
public class InfoController {

    // region - Class Variables -
    private final Environment environment;

    private final BuildProperties buildProperties;

    private final GitApiService gitApiService;

    private final LogService logService;
    // endregion - Class Variables -

    @Autowired
    public InfoController(Environment environment, BuildProperties buildProperties, GitApiService gitApiService, LogService logService) {
        this.environment = environment;
        this.buildProperties = buildProperties;
        this.gitApiService = gitApiService;
        this.logService = logService;
    }
    // Todo: More Info: homepage, wiki, socials, donations, feature requests

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiData getApiData() {
        log.info("Retrieving api data");

        return new ApiData(environment, buildProperties);
    }

    @GetMapping(value = "/git", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGitInfo() {
        log.info("Retrieving git info");

        return new ResponseEntity<>(gitApiService.getGitRelease(), HttpStatus.OK);
    }

    @GetMapping(value = "/logFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLogFiles() {
        log.info("Retrieving log files");

        return new ResponseEntity<>(logService.getAllLogFiles(), HttpStatus.OK);
    }

    @PostMapping(value = "/logFiles/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getLogFilePost(@RequestBody String filePath) {
        // TODO: scrub filePath here
        filePath = filePath.replaceAll("\"", "")
                .replaceAll("'", "");

        log.info("Generating/Retrieving log file {}", filePath);

        byte[] fileContent = logService.getLogFileContent(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"logFile\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }
}
