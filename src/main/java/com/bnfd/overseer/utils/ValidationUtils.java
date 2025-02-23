package com.bnfd.overseer.utils;


import org.apache.commons.lang3.*;

import java.lang.reflect.*;

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
}
