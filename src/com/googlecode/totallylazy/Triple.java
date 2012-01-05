package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Triple<F, S, T> extends Pair<F,S> implements Third<T> {
    protected final T third;

    public static <F, S, T> Triple<F, S, T> triple(final F first, final S second, final T third) {
        return new Triple<F, S, T>(first, second, third);
    }

    public Triple(final F first, final S second, final T third) {
        super(first, second);
        this.third = third;
    }

    public final T third() {
        return third;
    }

    @Override
    public Sequence<Object> values() {
        return sequence(first, second, third);
    }

}
