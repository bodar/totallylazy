package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Lazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.functions.Functions.returns;

public class Quintuple<F, S, T, Fo, Fi> extends Quadruple<F, S, T, Fo> implements Fifth<Fi> {
    private final Value<? extends Fi> fifth;

    public static <F, S, T, Fo, Fi> Quintuple<F, S, T, Fo, Fi> quintuple(final F first, final S second, final T third, final Fo fourth, final Fi fifth) {
        return new Quintuple<F, S, T, Fo, Fi>(returns(first), returns(second), returns(third), returns(fourth), returns(fifth));
    }

    public static <F, S, T, Fo, Fi> Quintuple<F, S, T, Fo, Fi> quintuple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth, final Callable<? extends Fi> fifth) {
        return new Quintuple<F, S, T, Fo, Fi>(first, second, third, fourth, fifth);
    }

    protected Quintuple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth, final Callable<? extends Fi> fifth) {
        super(first, second, third, fourth);
        this.fifth = Lazy.lazy(fifth);
    }

    public final Fi fifth() {
        return fifth.value();
    }

    @Override
    public Sequence<Object> values() {
        return super.values().append(fifth());
    }
}
