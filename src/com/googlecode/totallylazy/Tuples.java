package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.CurriedFunction2;
import com.googlecode.totallylazy.functions.Function2;

import static com.googlecode.totallylazy.Triple.triple;

public class Tuples {
    public static <F, S> CurriedFunction2<Pair<F, S>, Pair<F, S>, Pair<F, S>> to(final Function2<? super F, ? super F, ? extends F> first,
                                                                          final Function2<? super S, ? super S, ? extends S> second) {
        return (a, b) -> Pair.pair(first.call(a.first(), b.first()),
                second.call(a.second(), b.second()));
    }

    public static <F, S, T> CurriedFunction2<Triple<F, S, T>, Triple<F, S, T>, Triple<F, S, T>> to(final Function2<? super F, ? super F, ? extends F> first,
                                                                                            final Function2<? super S, ? super S, ? extends S> second,
                                                                                            final Function2<? super T, ? super T, ? extends T> triple) {
        return (a, b) -> triple(first.call(a.first(), b.first()),
                second.call(a.second(), b.second()),
                triple.call(a.third(), b.third()));
    }
}
