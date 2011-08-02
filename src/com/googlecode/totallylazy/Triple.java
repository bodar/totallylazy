package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class Triple<F, S, T> extends Tuple<F,S> implements Third<T> {
    private final T third;

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
    protected Sequence<Object> values() {
        return sequence(first, second, third);
    }

}
