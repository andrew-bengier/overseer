package com.bnfd.overseer.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static Instant offsetStringToInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, formatter);

        return offsetDateTime.toInstant();
    }

    public static boolean isArchiveEligible(Long added, Long lastViewed) {
        Instant twoYearsAgo = Instant.now().atOffset(ZoneOffset.UTC).minus(3, ChronoUnit.YEARS).toInstant();
        if (added < twoYearsAgo.toEpochMilli()) {
            return false;
        } else {
            return lastViewed == null || lastViewed < twoYearsAgo.toEpochMilli();
        }
    }
}
