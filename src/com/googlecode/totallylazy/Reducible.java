package com.googlecode.totallylazy;

public interface Reducible<A, S> {
    S reduce(S seed, Function2<? super S, ? super A, ? extends S> callable);

    default S reduce(Reducer<? super A, S> callable) {
        return reduce(callable.identityElement(), callable);
    }

    default <B> Reducible<B, S> map(Function<? super A, ? extends B> mapper) {
        return reducible(reducer ->
                (seed, input) ->
                        reducer.apply(seed, mapper.apply(input)));
    }

    default Reducible<A, S> filter(Predicate<? super A> predicate) {
        return reducible(reducer ->
                (seed, input) ->
                        predicate.matches(input) ? reducer.apply(seed, input) : seed);
    }

    default <B> Reducible<B, S> flatMap(Function<? super A, ? extends Reducible<B, S>> mapper) {
        return reducible(reducer ->
                (seed, input) ->
                        mapper.apply(input).reduce(seed, reducer));
    }

    default <B> Reducible<B, S> reducible(Function<
            Function2<? super S, ? super B, ? extends S>,
            Function2<? super S, ? super A, ? extends S>> transformer) {
        return (seed, reducer) ->
                reduce(seed, transformer.apply(reducer));
    }

}
