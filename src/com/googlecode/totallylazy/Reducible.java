package com.googlecode.totallylazy;

public interface Reducible<A> {
    <S> S reduce(S seed, Function2<? super S, ? super A, ? extends S> callable);

    default <S> S reduce(Reducer<S, ? super A> callable) {
        return reduce(callable.identityElement(), callable);
    }

    default <B> Reducible<B> map(Function<? super A, ? extends B> mapper) {
        return reducible(reducer ->
                (seed, input) ->
                        reducer.apply(seed, mapper.apply(input)));
    }

    default Reducible<A> filter(Predicate<? super A> predicate) {
        return reducible(reducer ->
                (seed, input) ->
                        predicate.matches(input) ? reducer.apply(seed, input) : seed);
    }

    default <B> Reducible<B> flatMap(Function<? super A, ? extends Reducible<B>> mapper) {
        return reducible(reducer ->
                (seed, input) ->
                        mapper.apply(input).reduce(seed, reducer));
    }

    default <B, S> Reducible<B> reducible(Function<? super Function2<S, B, S>, ? extends Function2<S, A, S>> transformer) {
        return new Reducible<B>() {
            @Override
            public <R> R reduce(R seed, Function2<? super R, ? super B, ? extends R> reducer) {
                return Reducible.this.reduce(seed, Unchecked.<Function<Function2, Function2<R, A, R>>>cast(transformer).apply(reducer));
            }
        };
    }
}
