package com.googlecode.totallylazy;

public interface Monoid<T> extends BinaryFunction<T>, Associative<T>, Identity<T>, Combiner<T, T> {
    static <A, B> Monoid<Pair<A, B>> pair(final Monoid<A> aCombiner, final Monoid<B> bCombiner) {
        return new Monoid<Pair<A, B>>() {
            @Override
            public Pair<A, B> call(Pair<A, B> a, Pair<A, B> b) throws Exception {
                return Pair.pair(aCombiner.call(a.first(), b.first()), bCombiner.call(a.second(), b.second()));
            }

            @Override
            public Pair<A, B> identity() {
                return Pair.pair(aCombiner.identity(), bCombiner.identity());
            }
        };
    }

    @Override
    default T combine(T a, T b) throws Exception {
        return call(a, b);
    }

}
