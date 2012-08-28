package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class Quadruple<F, S, T, Fo> extends Triple<F, S, T> implements Fourth<Fo> {
    private final Value<? extends Fo> fourth;

    public static <F, S, T, Fo> Quadruple<F, S, T, Fo> quadruple(final F first, final S second, final T third, final Fo fourth) {
        return new Quadruple<F, S, T, Fo>(returns(first), returns(second), returns(third), returns(fourth));
    }

    public static <F, S, T, Fo> Quadruple<F, S, T, Fo> quadruple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth) {
        return new Quadruple<F, S, T, Fo>(first, second, third, fourth);
    }

    protected Quadruple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third, final Callable<? extends Fo> fourth) {
        super(first, second, third);
        this.fourth = lazy(fourth);
    }

    public final Fo fourth() {
        return fourth.value();
    }

    @Override
    public Sequence<Object> values() {
        return super.values().add(fourth());
    }

}
