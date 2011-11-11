package com.googlecode.totallylazy.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
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
        simpleDateFormat.setLenient(false);
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
        GregorianCalendar calendar = calendar();
        calendar.set(YEAR, year);
        calendar.set(MONTH, month - 1);
        calendar.set(DAY_OF_MONTH, day);
        calendar.set(HOUR_OF_DAY, hour);
        calendar.set(MINUTE, minute);
        calendar.set(SECOND, second);
        calendar.set(MILLISECOND, milliSec);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int amount) {
        GregorianCalendar calendar = calendar(date);
        calendar.add(SECOND, amount);
        return calendar.getTime();
    }

    private static GregorianCalendar calendar() {
        return new GregorianCalendar(UTC);
    }

    private static GregorianCalendar calendar(Date date) {
        GregorianCalendar calendar = calendar();
        calendar.setTime(date);
        return calendar;
    }
}
