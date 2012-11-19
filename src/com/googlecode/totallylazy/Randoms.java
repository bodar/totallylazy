package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.time.Dates;
import com.googlecode.totallylazy.time.Days;

import java.util.Date;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.sort;
import static com.googlecode.totallylazy.time.Dates.stripTime;
import static java.lang.Math.abs;

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

    public static Sequence<Long> longs() {
        return Sequences.forwardOnly(new StatefulIterator<Long>() {
            @Override
            protected Long getNext() throws Exception {
                return random.nextLong();
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

    public static Sequence<Boolean> booleans() {
        return Sequences.forwardOnly(new StatefulIterator<Boolean>() {
            @Override
            protected Boolean getNext() throws Exception {
                return random.nextBoolean();
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

    public static <T> Sequence<T> values(final T... values) {
        return Sequences.forwardOnly(new StatefulIterator<T>() {
            @Override
            protected T getNext() throws Exception {
                return values[random.nextInt(values.length)];
            }
        });
    }

    public static Sequence<Integer> between(final int start, final int end) {
        final Pair<Integer, Integer> bounds = bounds(start, end);
        return Sequences.forwardOnly(new StatefulIterator<Integer>() {
            @Override
            protected Integer getNext() throws Exception {
                int range = bounds.second() - bounds.first() + 1;
                return random.nextInt(Math.max(1, abs(range))) + bounds.first();
            }
        });
    }

    public static Sequence<Double> between(final Double start, final Double end) {
        final Pair<Double, Double> bounds = bounds(start, end);
        return Sequences.forwardOnly(new StatefulIterator<Double>() {
            @Override
            protected Double getNext() throws Exception {
                return bounds.first() + random.nextDouble() * (bounds.second() - bounds.first());
            }
        });
    }

    public static Sequence<Date> between(final Date start, final Date end) {
        final Pair<Date, Date> bounds = bounds(start, end);
        return Sequences.forwardOnly(new StatefulIterator<Date>() {
            @Override
            protected Date getNext() throws Exception {
                return stripTime(Days.add(bounds.first(), between(0, daysBetween(bounds.first(), bounds.second())).head()));
            }

            private Integer daysBetween(Date start, Date end) {
                return abs((int) (end.getTime() - start.getTime())) / (24 * 60 * 60 * 1000);
            }
        });
    }

    public static Sequence<Date> after(final Date date) {
        return between(date, new Date(Long.MAX_VALUE));
    }

    private static <T extends Comparable<? super T>> Pair<T, T> bounds(T start, T end) {
        final Sequence<T> sorted = sort(sequence(start, end), Comparators.<T>ascending());
        return pair(sorted.first(), sorted.second());
    }
}