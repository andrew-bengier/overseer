package com.bnfd.overseer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "ApiKey Endpoints")
@RequestMapping("api/connections")
public class ConnectionController {

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> testConnection(HttpServletRequest request) throws Throwable {
        log.info(String.valueOf(request));

        return ResponseEntity.ok().build();
    }
}
