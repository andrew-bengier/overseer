package com.bnfd.overseer.model.mapper;

import com.bnfd.overseer.model.constants.SettingLevel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringSettingLevelConverter implements Converter<String, SettingLevel> {

    @Override
    public SettingLevel convert(String source) {
        try {
            return SettingLevel.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}