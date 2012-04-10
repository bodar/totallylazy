package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.time.Dates;
import com.googlecode.totallylazy.time.Days;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;
import static java.util.Calendar.DAY_OF_YEAR;

public class Randoms {
    private static java.util.Random random = new java.util.Random();

    public static Sequence<Integer> integers() {
        return Sequences.forwardOnly(new StatefulIterator<Integer>() {

            @Override
            protected Integer getNext() throws Exception {
                return random.nextInt();
            }
        });
    }

    public static Sequence<Integer> range(final int start, final int end) {
        checkRange(start, end);
        return Sequences.forwardOnly(new StatefulIterator<Integer>() {
            @Override
            protected Integer getNext() throws Exception {
                Integer range = end - start + 1;
                return random.nextInt(Math.max(1, abs(range))) + start;
            }
        });
    }

    public static Sequence<Long> longs() {
        return Sequences.forwardOnly(new StatefulIterator<Long>() {
            @Override
            protected Long getNext() throws Exception {
                return random.nextLong();
            }
        });
    }

    public static <T> Sequence<T> values(final T... values) {
        return Sequences.forwardOnly(new StatefulIterator<T>() {
            @Override
            protected T getNext() throws Exception {
                return values[random.nextInt(values.length)];
            }
        });
    }

    public static Sequence<Date> dates() {
        return Sequences.forwardOnly(new StatefulIterator<Date>() {
            @Override
            protected Date getNext() throws Exception {
                return stripTime(new Date(random.nextLong()));
            }
        });
    }

    public static Sequence<Date> range(final Date start, final Date end) {
        checkRange(start.getTime(), end.getTime());
        return Sequences.forwardOnly(new StatefulIterator<Date>() {
            @Override
            protected Date getNext() throws Exception {
                return stripTime(Days.add(start, range(0, daysBetween(start, end)).head()));
            }

            private Integer daysBetween(Date start, Date end) {
                return (int) (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
            }
        });
    }

    private static Date stripTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Sequence<Date> after(final Date date) {
        return range(date, new Date(Long.MAX_VALUE));
    }

    public static Sequence<Boolean> booleans() {
        return Sequences.forwardOnly(new StatefulIterator<Boolean>() {
            @Override
            protected Boolean getNext() throws Exception {
                return random.nextBoolean();
            }
        });
    }

    public static Sequence<Double> doubles() {
        return Sequences.forwardOnly(new StatefulIterator<Double>() {
            @Override
            protected Double getNext() throws Exception {
                return random.nextDouble();
            }
        });
    }

    public static Sequence<Double> range(final Double start, final Double end) {
        checkRange(start, end);
        return Sequences.forwardOnly(new StatefulIterator<Double>() {
            @Override
            protected Double getNext() throws Exception {
                return start + random.nextDouble() * (end - start);
            }
        });
    }

    private static void checkRange(double start, double end) {
        if (start > end) throw new IllegalArgumentException("start must be <= end");
    }

    private static void checkRange(long start, long end) {
        if (start > end) throw new IllegalArgumentException("start must be <= end");
    }
}