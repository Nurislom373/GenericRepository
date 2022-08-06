package org.khasanof.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Objects;


public class DateTimeFormatter {

    public static LocalDate dateParseLocalDate(Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("Date is null");
        }
        return date.toLocalDate();
    }

    public static Instant dateParseInstant(Date date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("Date is null");
        }
        return date.toInstant();
    }

    public static LocalTime timeParseLocalTime(Time time) {
        if (Objects.isNull(time)) {
            throw new IllegalArgumentException("Time is null");
        }
        return time.toLocalTime();
    }

    public static Instant timeParseInstant(Time time) {
        if (Objects.isNull(time)) {
            throw new IllegalArgumentException("Time is null");
        }
        return time.toInstant();
    }

    public static OffsetTime timeParseOffsetTime(Time time) {
        if (Objects.isNull(time)) {
            throw new IllegalArgumentException("Time is null");
        }
        return OffsetTime.of(time.toLocalTime(), ZoneOffset.UTC);
    }


    public static LocalDateTime timestampParseLocalDateTime(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            throw new IllegalArgumentException("Timestamp is null");
        }
        return timestamp.toLocalDateTime();
    }

    public static Instant timestampParseInstant(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            throw new IllegalArgumentException("Timestamp is null");
        }
        return timestamp.toInstant();
    }

}
