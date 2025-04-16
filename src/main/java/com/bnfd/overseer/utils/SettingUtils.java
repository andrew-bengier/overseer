package com.bnfd.overseer.utils;

import com.bnfd.overseer.config.DefaultSettings;
import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerPreConditionRequiredException;
import com.bnfd.overseer.model.constants.SettingLevel;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.model.persistence.SettingEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.bnfd.overseer.utils.Constants.DEFAULT_SETTINGS_FILE;
import static com.bnfd.overseer.utils.Constants.DEFAULT_SETTING_KEY;

public class SettingUtils {
    public static Map<String, String> getDefaultSettingsByLevel(SettingLevel settingLevel) {
        Map<String, TreeMap<String, String>> defaultSettings = DefaultSettings.getSettings();
        Map<String, String> settings = MapUtils.isNotEmpty(defaultSettings.get(SettingLevel.DEFAULT.name().toLowerCase())) ? new HashMap<>(defaultSettings.get(SettingLevel.DEFAULT.name().toLowerCase())) : new HashMap<>();

        if (settingLevel != SettingLevel.DEFAULT) {
            for (SettingLevel level : SettingLevel.values()) {
                System.out.println("Setting level: " + level);
                Map<String, String> levelSettings = defaultSettings.get(level.name().toLowerCase());
                if (MapUtils.isNotEmpty(levelSettings)) {
                    System.out.println("Settings: " + levelSettings.toString());
                    settings.putAll(levelSettings);
                }

                if (level == settingLevel) {
                    break;
                }
            }
        }

        return settings;
    }

    public static void updateDefaultSetting(SettingLevel level, Map<String, String> settings) {
        if (ObjectUtils.isEmpty(level)) {
            level = SettingLevel.DEFAULT;
        }

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(DEFAULT_SETTINGS_FILE)) {
            properties.load(input);
        } catch (IOException exception) {
            throw new OverseerPreConditionRequiredException("Could not load default settings from " + DEFAULT_SETTINGS_FILE);
        }

        for (Map.Entry<String, String> setting : settings.entrySet()) {
            String key = String.join(".", DEFAULT_SETTING_KEY, level.name().toLowerCase(), setting.getKey());
            properties.setProperty(key, setting.getValue());
        }

        try (FileOutputStream output = new FileOutputStream(DEFAULT_SETTINGS_FILE)) {
            properties.store(output, "Updated properties");
        } catch (IOException exception) {
            throw new OverseerException("Could not update default settings file " + DEFAULT_SETTINGS_FILE);
        }
    }

    public static SettingType getSettingType(String setting) {
        boolean isBoolean = "true".equalsIgnoreCase(setting) || "false".equalsIgnoreCase(setting);
        if (isBoolean) {
            return SettingType.BOOLEAN;
        }

        boolean isNumeric = StringUtils.isNumeric(setting);
        if (isNumeric) {
            return SettingType.NUMBER;
        }

        boolean isDate = GenericValidator.isDate(setting, LocaleContextHolder.getLocale());
        if (isDate) {
            return SettingType.DATE;
        }

        boolean isCron = ValidationUtils.isValidCron(setting);
        if (isCron) {
            return SettingType.CRON;
        }

        return SettingType.STRING;
    }

    public static Set<SettingEntity> syncSettings(String referenceId, Set<SettingEntity> currentSettings, Set<SettingEntity> requestedSettings, boolean isNew) {
        Set<SettingEntity> settings = new HashSet<>();

        if (CollectionUtils.isEmpty(currentSettings) && CollectionUtils.isEmpty(requestedSettings)) {
            return settings;
        } else if (CollectionUtils.isEmpty(requestedSettings)) {
            currentSettings.forEach(setting -> {
                setting.setId(UUID.randomUUID().toString());
                setting.setReferenceId(referenceId);
            });
            settings.addAll(currentSettings);
        } else {
            for (SettingEntity request : requestedSettings) {
                SettingEntity currentSetting = currentSettings.stream().filter(setting -> setting.getName().equalsIgnoreCase(request.getName())).findFirst().orElse(null);

                if (ObjectUtils.isEmpty(currentSetting)) {
                    // new setting
                    currentSetting = new SettingEntity();
                    currentSetting.setId(UUID.randomUUID().toString());
                    currentSetting.setReferenceId(referenceId);
                    currentSetting.setName(request.getName());
                    currentSetting.setVal(request.getVal());
                    currentSetting.setType(SettingUtils.getSettingType(request.getVal()));
                } else {
                    if (isNew) {
                        // new uuid, and maintain reference id
                        currentSetting.setId(UUID.randomUUID().toString());
                        currentSetting.setReferenceId(referenceId);
                    } else {
                        currentSetting.setVal(request.getVal());
                    }
                }

                settings.add(currentSetting);
            }
        }

        return settings;
    }

    public static Set<SettingEntity> syncDefaultSettings(String referenceId, Set<SettingEntity> currentSettings, SettingLevel level) {
        Set<SettingEntity> settings = new HashSet<>();

        Map<String, TreeMap<String, String>> defaultSettings = DefaultSettings.getSettings();
        for (SettingLevel settingLevel : Arrays.stream(SettingLevel.values()).filter(settingLevel -> settingLevel.ordinal() <= level.ordinal()).toList()) {
            if (defaultSettings.containsKey(settingLevel.name().toLowerCase())) {
                for (Map.Entry<String, String> defaultSetting : defaultSettings.get(settingLevel.name().toLowerCase()).entrySet()) {
                    SettingEntity currentSetting = currentSettings.stream().filter(setting -> setting.getName().equalsIgnoreCase(defaultSetting.getKey())).findFirst().orElse(null);

                    if (ObjectUtils.isEmpty(currentSetting)) {
                        // new setting
                        currentSetting = new SettingEntity();
                        currentSetting.setId(UUID.randomUUID().toString());
                        currentSetting.setReferenceId(referenceId);
                        currentSetting.setName(defaultSetting.getKey());
                        currentSetting.setVal(defaultSetting.getValue());
                        currentSetting.setType(SettingUtils.getSettingType(defaultSetting.getValue()));
                    } else {
                        // new uuid, and maintain reference id
                        currentSetting.setId(UUID.randomUUID().toString());
                        currentSetting.setReferenceId(referenceId);
                    }

                    settings.add(currentSetting);
                }
            }
        }

        return settings;
    }
}
