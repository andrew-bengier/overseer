package com.bnfd.overseer.utils;

import org.springframework.util.*;

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
}
