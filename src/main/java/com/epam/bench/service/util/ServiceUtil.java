package com.epam.bench.service.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Tetiana_Antonenko1
 */
public final class ServiceUtil {

    private static final String DATE_PATTERN_yyyyMMdd = "yyyy-MM-dd";

    private static final DateTimeFormatter formatter_yyyyMMdd = DateTimeFormatter.ofPattern(DATE_PATTERN_yyyyMMdd);

    private ServiceUtil() {
    }

    public static String getFormattedYearDate(final ZonedDateTime date) {
        return date.format(formatter_yyyyMMdd);
    }
}
