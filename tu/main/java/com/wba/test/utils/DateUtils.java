/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    public static Long convertStringToSec(Object dateSting, FormatTo format) {
        return dateSting == null ? null : ZonedDateTime.parse(dateSting.toString(), DateTimeFormatter.ofPattern(format.getFormat())).toEpochSecond();
    }

    public static Long convertStringToMilli(Object dateSting, FormatTo format) {
        return dateSting == null ? null : ZonedDateTime.parse(dateSting.toString(), DateTimeFormatter.ofPattern(format.getFormat())).toInstant().toEpochMilli();
    }

    public static Object calculateDateTime(String parameter, int output) {
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        if (!parameter.equals("0")) {
            final List<String[]> m = RegExp.getMatches(parameter, "^([+-]?\\d+)([smhdy])$");
            if (m.size() == 0)
                throw new IllegalArgumentException(String.format("Incorrect timestamp parameter: %s. Correct examples: -1s -5m +12h +1d -33y", parameter));

            final long shift = Long.parseLong(m.get(0)[0]);
            final String time = m.get(0)[1].toLowerCase();
            final TemporalUnit unit =
                    time.equals("s") ? ChronoUnit.SECONDS :
                            time.equals("m") ? ChronoUnit.MINUTES :
                                    time.equals("h") ? ChronoUnit.HOURS :
                                            time.equals("d") ? ChronoUnit.DAYS :
                                                    time.equals("y") ? ChronoUnit.YEARS : ChronoUnit.CENTURIES; // :)
            dateTime = dateTime.plus(shift, unit);
        }

        switch (output) {
            case 0: // 2018-06-07 21:45:01.818Z
                return dateTime.toOffsetDateTime().toString().replaceFirst("T", " ");
            case 1: // 2018-06-07T21:45:01.818Z
                return dateTime.toOffsetDateTime().toString();
            case 2: // 2018-06-07
                return dateTime.toLocalDate().toString();
            case 3: // 1528394013601
                return dateTime.toOffsetDateTime().toInstant().toEpochMilli();
            case 4: // 17702 (days from epoch)
                return Duration.between(ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.of("UTC")), dateTime).toDays();
            default:
                throw new IllegalArgumentException("format not found: " + output);
        }
    }


    public static String getMonthFromInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
}
