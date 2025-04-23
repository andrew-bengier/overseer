package com.bnfd.overseer.controller;

import com.bnfd.overseer.config.DefaultSettings;
import com.bnfd.overseer.model.constants.*;
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
    @GetMapping(path = "/settings/defaults", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDefaultSettings(@RequestParam(required = false) SettingLevel level) {
        if (level == null) {
            log.info("Retrieving default settings");
            return new ResponseEntity<>(DefaultSettings.getSettings(), HttpStatus.OK);
        } else {
            log.info("Retrieving default settings - level: {}", level);
            return new ResponseEntity<>(SettingUtils.getDefaultSettingsByLevel(level), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/builders/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBuilderOptions() {
        log.info("Retrieving builder options");

        return new ResponseEntity<>(lookupService.getAllBuilderOptions(), HttpStatus.OK);
    }

    // region - Constant / Enums -
    @GetMapping("/actions/categories")
    public ResponseEntity<?> getActionCategories() {
        return new ResponseEntity<>(ActionCategory.values(), HttpStatus.OK);
    }

    @GetMapping("/actions/types")
    public ResponseEntity<?> getActionTypes() {
        return new ResponseEntity<>(ActionType.values(), HttpStatus.OK);
    }

    @GetMapping("/apikeys/types")
    public ResponseEntity<?> getApiKeyTypes() {
        return new ResponseEntity<>(ApiKeyType.values(), HttpStatus.OK);
    }

    @GetMapping("/builders/categories")
    public ResponseEntity<?> getBuilderCategories() {
        return new ResponseEntity<>(BuilderCategory.values(), HttpStatus.OK);
    }

    @GetMapping("/builders/types")
    public ResponseEntity<?> getBuilderTypes() {
        return new ResponseEntity<>(BuilderType.values(), HttpStatus.OK);
    }

    @GetMapping("/collections/trackingTypes")
    public ResponseEntity<?> getCollectionTrackingTypes() {
        return new ResponseEntity<>(CollectionTrackingType.values(), HttpStatus.OK);
    }

    @GetMapping("/media/types")
    public ResponseEntity<?> getMediaTypes() {
        return new ResponseEntity<>(com.bnfd.overseer.model.constants.MediaType.values(), HttpStatus.OK);
    }

    @GetMapping("/mediaIds/types")
    public ResponseEntity<?> getMediaIdTypes() {
        return new ResponseEntity<>(MediaIdType.values(), HttpStatus.OK);
    }

    @GetMapping("/mediaImages/types")
    public ResponseEntity<?> getMediaImageTypes() {
        return new ResponseEntity<>(MediaImageType.values(), HttpStatus.OK);
    }

    @GetMapping("/settings/levels")
    public ResponseEntity<?> getSettingLevels() {
        return new ResponseEntity<>(SettingLevel.values(), HttpStatus.OK);
    }

    @GetMapping("/settings/types")
    public ResponseEntity<?> getSettingTypes() {
        return new ResponseEntity<>(SettingType.values(), HttpStatus.OK);
    }
    // endregion - Constant / Enums -
    // endregion - GET -

    // region - PUT -
    // NOTE: this endpoint will cause a context refresh
    @PutMapping(value = "/settings/defaults", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDefaultSettings(@RequestParam(required = false, defaultValue = "default") String level, @RequestBody Map<String, String> settings) throws Throwable {
        log.info("Updating default settings - level: {}", level);

        validationService.validateDefaultSettingsUpdateRequest(level, settings.keySet());

        SettingLevel settingLevel = SettingLevel.findByName(level);
        SettingUtils.updateDefaultSetting(settingLevel, settings);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    // endregion - PUT -
}
