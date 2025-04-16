package com.bnfd.overseer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.TreeMap;

@Configuration
@PropertySource("classpath:defaultSettings.properties")
@ConfigurationProperties(prefix = "default")
public class DefaultSettings {
    // region - Class Variables -
    private static TreeMap<String, TreeMap<String, String>> settings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    // endregion - Class Variables -

    // region - Accessor Methods -
    public static Map<String, TreeMap<String, String>> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, TreeMap<String, String>> settings) {
        DefaultSettings.settings = new TreeMap<>(settings);
    }
    // endregion - Accessor Methods -
}
