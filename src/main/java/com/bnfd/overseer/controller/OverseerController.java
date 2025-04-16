package com.bnfd.overseer.controller;

import com.bnfd.overseer.OverseerApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Control Endpoints")
@RequestMapping("api")
public class OverseerController {

    @PostMapping("/restart")
    public void restart() {
        OverseerApplication.restart();
    }
}
