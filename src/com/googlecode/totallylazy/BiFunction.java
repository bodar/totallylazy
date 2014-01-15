package com.googlecode.totallylazy;


import static com.googlecode.totallylazy.Unchecked.cast;

public interface BiFunction<A, B, C> extends java.util.function.BiFunction<A, B, C> {
    C call(A a, B b) throws Exception;

    default Function<A, Function<B, C>> curry() {
        return (a) -> (b) -> call(a, b);
    }

    default Function<B, C> apply(final A a) {
        return Functions.apply(this, a);
    }

    default Function<B, C> call(final A a) throws Exception {
        return Functions.apply(this, a);
    }

    default C apply(final A a, final B b) {
        return Functions.call(this, a, b);
    }

    default Function<A, C> applySecond(final B b) {
        return flip().apply(b);
    }

    default Returns<C> deferApply(final A a, final B b) {
        return Callables.deferApply(this, a, b);
    }

    default BiFunction<B, A, C> flip() {
        return Callables.flip(this);
    }

    default Function<Pair<A, B>, C> pair() {
        return Callables.pair(this);
    }

    default <D> BiFunction<A, B, D> then(Function<? super C, ? extends D> after) {
        return (a, b) -> after.call(call(a, b));
    }

    default <D, E> BiFunction<A, B, Function<D, E>> then(BiFunction<? super C, ? super D, ? extends E> after) {
        return then(Unchecked.<Function<C, Function<D, E>>>cast(after.curry()));
    }
}
