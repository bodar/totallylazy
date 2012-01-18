package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

public class Functions {
    public static <A> Function<A> function(final Callable<? extends A> callable) {
        return Function.function(callable);
    }

    public static <A, B> Function1<A, B> function(final Callable1<A, B> callable) {
        return Function1.function(callable);
    }

    public static <A, B, C> Function2<A, B, C> function(final Callable2<A, B, C> callable) {
        return Function2.function(callable);
    }
}
