package com.bnfd.overseer.utils;

import com.bnfd.overseer.model.constants.*;
import com.cronutils.model.*;
import com.cronutils.model.definition.*;
import com.cronutils.parser.*;
import org.apache.commons.lang3.*;
import org.apache.commons.validator.*;
import org.springframework.context.i18n.*;

public class SettingUtils {
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

        boolean isCron = isValidCron(setting);
        if (isCron) {
            return SettingType.CRON;
        }

        return SettingType.STRING;
    }

    // NOTE: only QUARTZ is currently supported
    protected static boolean isValidCron(String cronExpression) {
        try {
            CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
            parser.parse(cronExpression);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
