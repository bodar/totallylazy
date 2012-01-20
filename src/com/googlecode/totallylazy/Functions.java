package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

public class Functions {
    public static <A> Function<A> function(final Callable<? extends A> callable) {
        return Function.function(callable);
    }

    public static <A, B> Function1<A, B> function(final Callable1<? super A, ? extends B> callable) {
        return Function1.function(callable);
    }

    public static <A, B, C> Function2<A, B, C> function(final Callable2<? super A, ? super B, ? extends C> callable) {
        return Function2.function(callable);
    }

    public static Function1<Pair<Boolean, Boolean>, Boolean> and() {
        return new Function1<Pair<Boolean, Boolean>, Boolean>() {
            @Override
            public Boolean call(Pair<Boolean, Boolean> pair) throws Exception {
                return pair.first() && pair.second();
            }
        };
    }

    public static Function1<Pair<Boolean, Boolean>, Boolean> or() {
        return new Function1<Pair<Boolean, Boolean>, Boolean>() {
            @Override
            public Boolean call(Pair<Boolean, Boolean> pair) throws Exception {
                return pair.first() || pair.second();
            }
        };
    }
}
