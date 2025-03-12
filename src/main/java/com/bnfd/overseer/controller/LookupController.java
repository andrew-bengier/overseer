package com.bnfd.overseer.controller;

import com.bnfd.overseer.config.DefaultSettings;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.service.LookupService;
import com.bnfd.overseer.service.ValidationService;
import com.bnfd.overseer.utils.SettingUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Lookup Endpoints")
@RequestMapping("api/lookups")
@CrossOrigin(origins = "http://localhost:3000")
public class LookupController {
    // region - Class Variables -
    private final LookupService lookupService;
    private final ValidationService validationService;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LookupController(LookupService lookupService, ValidationService validationService) {
        this.lookupService = lookupService;
        this.validationService = validationService;
    }
    // endregion - Constructors -

    // region - GET -
    @GetMapping(path = "/builders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBuilders() {
        log.info("Retrieving builders");

        return new ResponseEntity<>(lookupService.getAllBuilders(), HttpStatus.OK);
    }

    @GetMapping(path = "/defaultSettings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDefaultSettings(@RequestParam(required = false) SettingLevel level) {
        if (level == null) {
            log.info("Retrieving default settings");
            return new ResponseEntity<>(DefaultSettings.getSettings(), HttpStatus.OK);
        } else {
            log.info("Retrieving default settings - level: {}", level);
            return new ResponseEntity<>(SettingUtils.getDefaultSettingsByLevel(level), HttpStatus.OK);
        }
    }
    // endregion - GET -

    // region - PUT -
    // NOTE: this endpoint will cause a context refresh
    @PutMapping(value = "/defaultSettings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDefaultSettings(@RequestParam(required = false, defaultValue = "default") String level, @RequestBody Map<String, String> settings) throws Throwable {
        log.info("Updating default settings - level: {}", level);

        validationService.validateDefaultSettingsUpdateRequest(level, settings.keySet());

        SettingLevel settingLevel = SettingLevel.findByName(level);
        SettingUtils.updateDefaultSetting(settingLevel, settings);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    // endregion - PUT -
}
