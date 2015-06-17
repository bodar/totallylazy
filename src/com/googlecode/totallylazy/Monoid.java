package com.googlecode.totallylazy;

public interface Monoid<T> extends ReducerCombiner<T, T>, Associative<T> {
    class functions {
        public static <A, B> CombinerFunction<Pair<A, B>> pair(final Monoid<A> aMonoid, final Monoid<B> bMonoid) {
            return new CombinerFunction<Pair<A, B>>() {
                @Override
                public Pair<A, B> call(Pair<A, B> a, Pair<A, B> b) throws Exception {
                    return Pair.pair(aMonoid.call(a.first(), b.first()), bMonoid.call(a.second(), b.second()));
                }

                @Override
                public Pair<A, B> identity() {
                    return Pair.pair(aMonoid.identity(), bMonoid.identity());
                }
            };
        }
    }
}
