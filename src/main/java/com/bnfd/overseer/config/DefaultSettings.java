package com.bnfd.overseer.config;

import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
@PropertySource("classpath:defaultSettings.properties")
@ConfigurationProperties(prefix = "default")
public class DefaultSettings {
    // region - Class Variables -
    private static TreeMap<String, String> settings;
    // endregion - Class Variables -

    // region - Accessor Methods -
    public static Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        DefaultSettings.settings = new TreeMap<>(settings);
    }
    // endregion - Accessor Methods -
}
