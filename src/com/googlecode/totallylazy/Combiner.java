package com.googlecode.totallylazy;

public interface Combiner<T> extends ReducerCombiner<T, T>, Associative<T> {
    @Override
    default T combine(T a, T b) throws Exception {
        return call(a, b);
    }

    static <A> Combiner<A> combiner(A identity, Function2<A,A,A> function){
        return new Combiner<A>() {
            @Override
            public A call(A a, A a2) throws Exception {
                return function.call(a, a2);
            }

            @Override
            public A identityElement() {
                return identity;
            }
        };
    }

    static <A, B> Combiner<Pair<A, B>> pair(final Combiner<A> aCombiner, final Combiner<B> bCombiner) {
        return new Combiner<Pair<A, B>>() {
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
