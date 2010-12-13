package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Predicates.notNull;
import static com.googlecode.totallylazy.Sequences.iterate;

public class Exceptions {
    public static <T extends Throwable> Option<T> find(final Throwable throwable, final Class<T> aClass) {
        return causes(throwable).
                safeCast(aClass).
                headOption();
    }

    public static Sequence<Throwable> causes(Throwable throwable) {
        return iterate(getCause(), throwable).
                takeWhile(notNull(Throwable.class));
    }

    public static Callable1<Throwable, Throwable> getCause() {
        return new Callable1<Throwable, Throwable>() {
            public Throwable call(final Throwable throwable) throws Exception {
                return throwable.getCause();
            }
        };
    }
}
