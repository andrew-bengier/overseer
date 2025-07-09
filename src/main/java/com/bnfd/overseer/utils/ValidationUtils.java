package com.bnfd.overseer.utils;


import com.bnfd.overseer.model.media.plex.components.Collection;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class ValidationUtils {
    public static boolean isEmpty(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            return true;
        } else {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            return fields.length == 0;
        }
    }

    public static Boolean convertStringToBoolean(String value) {
        Boolean result = null;
        if (StringUtils.isNotBlank(value)) {
            if (value.equalsIgnoreCase("true")
                    || value.equalsIgnoreCase("t")
                    || value.equalsIgnoreCase("on")
                    || value.equalsIgnoreCase("1")) {
                result = Boolean.TRUE;
            }

            if (value.equalsIgnoreCase("false")
                    || value.equalsIgnoreCase("f")
                    || value.equalsIgnoreCase("off")
                    || value.equalsIgnoreCase("0")) {
                result = Boolean.FALSE;
            }
        }

        return result;
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

    public static boolean isArchived(List<Collection> collections) {
        if (CollectionUtils.isEmpty(collections)) {
            return false;
        } else {
            return collections.stream().anyMatch(collection -> collection.getTag().equalsIgnoreCase("archive"));
        }
    }
}
