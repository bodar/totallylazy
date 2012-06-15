package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Function.returns;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class Triple<F, S, T> extends Pair<F, S> implements Third<T> {
    private final Value<? extends T> third;

    public static <F, S, T> Triple<F, S, T> triple(final F first, final S second, final T third) {
        return new Triple<F, S, T>(returns(first), returns(second), returns(third));
    }

    public static <F, S, T> Triple<F, S, T> triple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third) {
        return new Triple<F, S, T>(first, second, third);
    }

    protected Triple(final Callable<? extends F> first, final Callable<? extends S> second, final Callable<? extends T> third) {
        super(first, second);
        this.third = lazy(third);
    }

    public final T third() {
        return third.value();
    }

    @Override
    public Sequence<Object> values() {
        return super.values().add(third());
    }

    public static <A, B, C, D>  Function2<Triple<A, B, C>, D, Triple<B, C, D>> leftShift() {
        return new Function2<Triple<A, B, C>, D, Triple<B, C, D>>() {
            @Override
            public Triple<B, C, D> call(Triple<A, B, C> triple, D d) throws Exception {
                return leftShift(triple, d);
            }
        };
    }

    public static <A, B, C, D> Triple<B, C, D> leftShift(Triple<A, B, C> triple, D d) {
        return Triple.triple(triple.second(), triple.third(), d);
    }


}
