package com.bnfd.overseer.exception;

import java.lang.reflect.*;
import java.util.*;

public class ErrorCreator {
    public static Throwable createThrowable(Class<? extends Throwable> throwableClass, List<String> errors) {
        try {
            Constructor<? extends Throwable> constructor = throwableClass.getConstructor(List.class);
            return constructor.newInstance(errors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create overseer error", e);
        }
    }
}
