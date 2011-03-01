package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Predicates.*;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

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

    public static <T,S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Predicate<? super Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses));
    }

    public static <T,S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Iterable<? extends Predicate<? super Exception>> predicates) {
        return new Callable1<T, Option<S>>() {
            public Option<S> call(T t) throws Exception {
                try {
                    return Option.some(callable.call(t));
                } catch (Exception e) {
                    if(sequence(predicates).exists(Predicates.<Exception>matches(e))){
                        return none();
                    }
                    throw e;
                }
            }
        };
    }
}
