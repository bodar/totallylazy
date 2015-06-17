package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Triple.triple;

public class Tuples {
    public static <F, S> Curried2<Pair<F, S>, Pair<F, S>, Pair<F, S>> to(final Function2<? super F, ? super F, ? extends F> first,
                                                                          final Function2<? super S, ? super S, ? extends S> second) {
        return new Curried2<Pair<F, S>, Pair<F, S>, Pair<F, S>>() {
            @Override
            public Pair<F, S> call(Pair<F, S> a, Pair<F, S> b) throws Exception {
                return Pair.pair(first.call(a.first(), b.first()),
                        second.call(a.second(), b.second()));
            }
        };
    }

    public static <F, S, T> Curried2<Triple<F, S, T>, Triple<F, S, T>, Triple<F, S, T>> to(final Function2<? super F, ? super F, ? extends F> first,
                                                                                            final Function2<? super S, ? super S, ? extends S> second,
                                                                                            final Function2<? super T, ? super T, ? extends T> triple) {
        return new Curried2<Triple<F, S, T>, Triple<F, S, T>, Triple<F, S, T>>() {
            @Override
            public Triple<F, S, T> call(Triple<F, S, T> a, Triple<F, S, T> b) throws Exception {
                return triple(first.call(a.first(), b.first()),
                        second.call(a.second(), b.second()),
                        triple.call(a.third(), b.third()));
            }
        };
    }
}
