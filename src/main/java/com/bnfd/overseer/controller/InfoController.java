package com.bnfd.overseer.controller;

import com.bnfd.overseer.model.api.info.ApiData;
import com.bnfd.overseer.model.api.info.LogFile;
import com.bnfd.overseer.service.LogService;
import com.bnfd.overseer.service.api.GitApiService;
import com.bnfd.overseer.utils.HttpUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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
    public ResponseEntity<?> getLogFiles(@RequestParam(required = false, value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
                                         @RequestParam(required = false, value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        if (startDate != null && endDate != null) {
            log.info("Retrieving log files between {} - {}", startDate, endDate);
        } else if (startDate != null) {
            log.info("Retrieving log files after {}", startDate);
        } else if (endDate != null) {
            log.info("Retrieving log files before {}", endDate);
        } else {
            log.info("Retrieving log files");
        }

        Page<LogFile> response = new PageImpl<>(logService.getAllLogFiles(startDate, endDate));

        return new ResponseEntity<>(response.getContent(), HttpUtils.generateHeaders(response), HttpStatus.OK);
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
