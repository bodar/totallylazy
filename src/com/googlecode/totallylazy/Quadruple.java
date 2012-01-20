package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Quadruple<F, S, T, Fo> extends Triple<F, S, T> implements Fourth<Fo> {
    protected final Fo fourth;

    public static <F, S, T, Fo> Quadruple<F, S, T, Fo> quadruple(final F first, final S second, final T third, final Fo fourth) {
        return new Quadruple<F, S, T, Fo>(first, second, third, fourth);
    }

    public Quadruple(final F first, final S second, final T third, final Fo fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    public final Fo fourth() {
        return fourth;
    }

    @Override
    public Sequence<Object> values() {
        return sequence(first(), second(), third, fourth);
    }

}
