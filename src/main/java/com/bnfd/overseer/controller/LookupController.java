package com.bnfd.overseer.controller;

import com.bnfd.overseer.service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/lookups")
@CrossOrigin(origins = "http://localhost:3000")
public class LookupController {
    // region - Class Variables -
    private final LookupService lookupService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }
    // endregion - Constructors -


    // region - GET -
    @GetMapping(value = "/actions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActionsByCategory(@RequestParam String category) {
        log.info(String.format("Retrieving %s actions", category));

        return new ResponseEntity<>(lookupService.getActionsByCategory(category), HttpStatus.OK);
    }
    // endregion - GET -
}
