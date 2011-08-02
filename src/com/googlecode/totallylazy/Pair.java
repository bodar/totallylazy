package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class Pair<F, S> extends Tuple<F,S> {
    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(first, second);
    }

    public Pair(final F first, final S second) {
        super(first, second);
    }

    @Override
    protected Sequence<Object> values() {
        return sequence(first, second);
    }

}
