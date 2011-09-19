package com.googlecode.totallylazy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Dates {
    public static final String RFC3339 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String RFC822 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String JAVA_UTIL_DATE_TO_STRING = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static DateFormat RFC3339() {
        return format(RFC3339);
    }

    private static SimpleDateFormat format(final String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(UTC);
        return simpleDateFormat;
    }

    public static DateFormat RFC822() {
        return format(RFC822);
    }

    public static DateFormat javaUtilDateToString() {
        return format(JAVA_UTIL_DATE_TO_STRING);
    }

    public static Date date(int year, int month, int day) {
        return date(year, month, day, 0);
    }

    public static Date date(int year, int month, int day, int hour) {
        return date(year, month, day, hour, 0);
    }

    public static Date date(int year, int month, int day, int hour, int minute) {
        return date(year, month, day, hour, minute, 0);
    }

    public static Date date(int year, int month, int day, int hour, int minute, int second) {
        return date(year, month, day, hour, minute, second, 0);
    }

    public static Date date(int year, int month, int day, int hour, int minute, int second, int milliSec) {
        GregorianCalendar calendar = new GregorianCalendar(UTC);
        calendar.set(YEAR, year);
        calendar.set(MONTH, month - 1);
        calendar.set(DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSec);
        return calendar.getTime();
    }
}
