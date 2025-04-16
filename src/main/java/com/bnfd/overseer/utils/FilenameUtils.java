package com.bnfd.overseer.utils;

import java.text.*;
import java.util.regex.*;

public class FilenameUtils {
    private static final Pattern INVALID_FILENAME_CHARS = Pattern.compile("[\\\\/:*?\"<> |\\x00-\\x1F]");
    private static final Pattern DUPLICATE_CHARS = Pattern.compile("(_)\\1+");
    private static final int MAX_FILENAME_LENGTH = 255;

    public static String sanitizeFilename(String filename) {
        String cleanFilename = filename;

        // Remove invalid characters and duplicates
        cleanFilename = INVALID_FILENAME_CHARS.matcher(cleanFilename).replaceAll("_");
        cleanFilename = DUPLICATE_CHARS.matcher(cleanFilename).replaceAll("$1");

        // Normalize
        cleanFilename = Normalizer.normalize(cleanFilename, Normalizer.Form.NFKC);

        // Remove trailing spaces and dots
        cleanFilename = cleanFilename.replaceAll("[. _]+$", "");

        // Truncate if necessary
        if (cleanFilename.length() > MAX_FILENAME_LENGTH) {
            cleanFilename = cleanFilename.substring(0, MAX_FILENAME_LENGTH);
        }

        return cleanFilename;
    }
}
