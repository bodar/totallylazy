package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Pair;

public interface Monoid<T> extends Combiner<T, T>, Associative<T> {
    @Override
    default T combine(T a, T b) throws Exception {
        return call(a, b);
    }

    static <T> Monoid<T> monoid(T identity, BinaryFunction<T> binary){
        return new Monoid<T>() {
            @Override
            public T call(T t, T t2) throws Exception {
                return binary.call(t, t2);
            }

            @Override
            public T identity() {
                return identity;
            }
        };
    }

    class functions {
        public static <A, B> CurriedMonoid<Pair<A, B>> pair(final Monoid<A> aMonoid, final Monoid<B> bMonoid) {
            return new CurriedMonoid<Pair<A, B>>() {
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
