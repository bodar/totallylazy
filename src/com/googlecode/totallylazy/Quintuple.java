package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Quintuple<F, S, T, Fo, Fi> extends Quadruple<F, S, T, Fo> implements Fifth<Fi> {
    protected final Fi fifth;

    public static <F, S, T, Fo, Fi> Quintuple<F, S, T, Fo, Fi> quintuple(final F first, final S second, final T third, final Fo fourth, final Fi fifth) {
        return new Quintuple<F, S, T, Fo, Fi>(first, second, third, fourth, fifth);
    }

    public Quintuple(final F first, final S second, final T third, final Fo fourth, final Fi fifth) {
        super(first, second, third, fourth);
        this.fifth = fifth;
    }

    public final Fi fifth() {
        return fifth;
    }

    @Override
    public Sequence<Object> values() {
        return sequence(first(), second(), third, fourth, fifth);
    }
}
