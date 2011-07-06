package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.numbers.Numbers;

public class Aggregate<T, R> extends Keyword<T> implements Callable2<T, T, R> {
    private final Callable2<? super T, ? super T, R> callable;

    public Aggregate(Callable2<? super T, ? super T, R> callable, Keyword<T> keyword) {
        super(keyword.name(), keyword.forClass());
        this.callable = callable;
    }

    public R call(T accumulator, T next) throws Exception {
        return callable.call(accumulator, next);
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

}
