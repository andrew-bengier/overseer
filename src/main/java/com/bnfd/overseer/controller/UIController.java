package com.bnfd.overseer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {
    @GetMapping({"/", "", "/{x:^(?!api$).*$}/*/{y:[\\\\w\\\\-]+}"})
    public HttpEntity<Void> redirectToIndex() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/index.html")
                .build();
    }
}
