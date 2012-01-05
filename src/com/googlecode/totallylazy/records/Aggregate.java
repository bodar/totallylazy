package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.numbers.Numbers;

import static java.lang.String.format;

public class Aggregate<T, R> extends AliasedKeyword<T> implements Callable2<T, T, R> {
    private final Callable2<? super T, ? super T, R> callable;

    public Aggregate(final Callable2<? super T, ? super T, R> callable, final Keyword<T> keyword, final String name) {
        super(keyword, name);
        this.callable = callable;
    }

    public Aggregate(final Callable2<? super T, ? super T, R> callable, final Keyword<T> keyword) {
        this(callable, keyword, generateName(callable, keyword));
    }

    private static <T, R> String generateName(final Callable2<? super T, ? super T, R> callable, final Keyword<T> keyword) {
        return format("%s_%s", callable.getClass().getSimpleName(), replaceIllegalCharacters(keyword.name())).toLowerCase();
    }

    private static String replaceIllegalCharacters(String name) {
        return name.replace("*", "star");
    }

    public R call(final T accumulator, final T next) throws Exception {
        return callable.call(accumulator, next);
    }

    public Callable2<? super T, ? super T, R> callable() {
        return callable;
    }

    public static <T, R> Aggregate<T, R> aggregate(Callable2<? super T, ? super T, R> callable, Keyword<T> keyword) {
        return new Aggregate<T, R>(callable, keyword);
    }

    public static <T extends Comparable<T>> Aggregate<T, T> maximum(Keyword<T> keyword) {
        return aggregate(Maximum.<T>maximum(), keyword);
    }

    public static <T extends Comparable<T>> Aggregate<T, T> minimum(Keyword<T> keyword) {
        return aggregate(Minimum.<T>minimum(), keyword);
    }

    public static <T extends Number> Aggregate<T, Number> sum(Keyword<T> keyword) {
        return aggregate(Numbers.<T>sum(), keyword);
    }

    public static <T extends Number> Aggregate<T, Number> average(Keyword<T> keyword) {
        return aggregate(Numbers.<T>average(), keyword);
    }

    @SuppressWarnings("unchecked")
    public Aggregate<T, R> as(Keyword<T> keyword) {
        return new Aggregate<T, R>(callable, source(), keyword.name());
    }
}
