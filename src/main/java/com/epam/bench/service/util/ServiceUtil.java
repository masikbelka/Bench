package com.epam.bench.service.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

/**
 * Created by Tetiana_Antonenko1
 */
public final class ServiceUtil {

    private static final String STANDART_MESSAGE = "validation.error.blank.parameter.message";
    @Inject
    private static MessageSource messageSource;

    private static final String DATE_PATTERN_yyyyMMdd = "yyyy-MM-dd";

    private static final DateTimeFormatter formatter_yyyyMMdd = DateTimeFormatter.ofPattern(DATE_PATTERN_yyyyMMdd);

    private ServiceUtil() {
    }

    public static String getFormattedYearDate(final ZonedDateTime date) {
        return date.format(formatter_yyyyMMdd);
    }

    public static void validateParameterNotBlank(String string) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(messageSource.getMessage(STANDART_MESSAGE, null, Locale.ENGLISH));
        }
    }
}
