package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static final String RFC3339_WITH_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String RFC822 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String JAVA_UTIL_DATE_TO_STRING = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String LEXICAL = "yyyyMMddHHmmssSSS";
    public static final String APACHE = "dd/MMM/yyyy:HH:mm:ss Z";

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    public static final Date MIN_VALUE = date(Long.MIN_VALUE);
    public static final Date MAX_VALUE = date(Long.MAX_VALUE);
    @Deprecated
    public static final String LUCENE = LEXICAL;

    @Deprecated
    public static DateFormat LUCENE() {
        return format(LUCENE);
    }

    public static DateFormatConverter RFC3339() {
        return new DateFormatConverter(RFC3339_WITH_MILLISECONDS, RFC3339);
    }

    public static SimpleDateFormat format(final String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(UTC);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    public static Function1<Date, String> format(final DateFormat format) {
        return new Function1<Date, String>() {
            @Override
            public String call(Date value) throws Exception {
                return format.format(value);
            }
        };
    }

    public static DateFormat APACHE() {
        return format(APACHE);
    }

    public static DateFormat LEXICAL() {
        return format(LEXICAL);
    }

    public static DateFormat RFC822() {
        return format(RFC822);
    }

    public static DateFormat RFC3339withMilliseconds() {
        return format(RFC3339_WITH_MILLISECONDS);
    }

    public static DateFormat javaUtilDateToString() {
        return format(JAVA_UTIL_DATE_TO_STRING);
    }

    public static Date parse(String value) {
        return date(value);
    }

    public static Function1<String, Date> parse(final DateFormat format) {
        return new Function1<String, Date>() {
            @Override
            public Date call(String value) throws Exception {
                return format.parse(value);
            }
        };
    }

    public static Function1<Date, Integer> millisecond() {
        return calendarField(Calendar.MILLISECOND);
    }

    public static Function1<Date, Integer> second() {
        return calendarField(Calendar.SECOND);
    }

    public static Function1<Date, Integer> minute() {
        return calendarField(Calendar.MINUTE);
    }

    public static Function1<Date, Integer> hourOfDay() {
        return calendarField(Calendar.HOUR_OF_DAY);
    }

    public static Function1<Date, Integer> dayOfWeek() {
        return calendarField(Calendar.DAY_OF_WEEK);
    }

    public static Function1<Date, Integer> dayOfMonth() {
        return calendarField(Calendar.DAY_OF_MONTH);
    }

    public static Function1<Date, Integer> weekOfMonth() {
        return calendarField(Calendar.WEEK_OF_MONTH);
    }

    public static Function1<Date, Integer> month() {
        return calendarField(Calendar.MONTH);
    }

    public static Function1<Date, Integer> dayOfYear() {
        return calendarField(Calendar.DAY_OF_YEAR);
    }

    public static Function1<Date, Integer> year() {
        return calendarField(Calendar.YEAR);
    }


    public static Function1<Date, Integer> calendarField(final int field) {
        return new Function1<Date, Integer>() {
            @Override
            public Integer call(Date value) throws Exception {
                return calendar(value).get(field);
            }
        };
    }

    public static Date date(String value) {
        return DateFormatConverter.defaultConverter().parse(value);
    }

    public static Date date(long value) {
        return new Date(value);
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

    @Deprecated
    public static Date addSeconds(Date date, int amount) {
        return Seconds.add(date, amount);
    }

    public static GregorianCalendar calendar() {
        return new GregorianCalendar(UTC);
    }

    public static GregorianCalendar calendar(Date date) {
        GregorianCalendar calendar = calendar();
        calendar.setTime(date);
        return calendar;
    }

    public static Date add(Date date, int timeUnit, int amount) {
        GregorianCalendar calendar = Dates.calendar(date);
        calendar.add(timeUnit, amount);
        return calendar.getTime();
    }

    public static Date subtract(Date date, int timeUnit, int amount) {
        return add(date, timeUnit, -amount);
    }

    public static Date stripTime(Date date) {
        Calendar calendar = calendar();
        calendar.setTime(date);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Maximum.Function<Date> maximum() {
        return Maximum.constructors.maximum(Dates.MIN_VALUE);
    }

    public static Minimum.Function<Date> minimum() {
        return Minimum.constructors.minimum(Dates.MAX_VALUE);
    }

    public static Date date(Date date) {
        return new Date(date.getTime());
    }
}