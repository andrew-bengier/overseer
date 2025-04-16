package com.bnfd.overseer.utils;

import java.time.*;
import java.time.format.*;

public class DateUtils {
    public static Instant offsetStringToInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, formatter);

        return offsetDateTime.toInstant();
    }
}
