package com.googlecode.totallylazy;

public interface Combiner<T> extends ReducerCombiner<T, T>, Associative<T> {
    class functions {
        public static <A, B> CombinerFunction<Pair<A, B>> pair(final Combiner<A> aCombiner, final Combiner<B> bCombiner) {
            return new CombinerFunction<Pair<A, B>>() {
                @Override
                public Pair<A, B> call(Pair<A, B> a, Pair<A, B> b) throws Exception {
                    return Pair.pair(aCombiner.call(a.first(), b.first()), bCombiner.call(a.second(), b.second()));
                }

                @Override
                public Pair<A, B> identityElement() {
                    return Pair.pair(aCombiner.identityElement(), bCombiner.identityElement());
                }
            };
        }
    }
}
