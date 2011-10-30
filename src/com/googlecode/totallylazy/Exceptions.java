package com.googlecode.totallylazy;

import java.io.PrintWriter;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Exceptions {
    public static Exception toException(Throwable throwable) throws Exception {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        return (Exception) throwable;
    }

    public static <T extends Throwable> Option<T> find(final Throwable throwable, final Class<T> aClass) {
        return causes(throwable).
                safeCast(aClass).
                headOption();
    }

    public static Sequence<Throwable> causes(Throwable throwable) {
        return iterate(getCause(), throwable).
                takeWhile(notNullValue());
    }

    public static Callable1<Throwable, Throwable> getCause() {
        return new Callable1<Throwable, Throwable>() {
            public Throwable call(final Throwable throwable) throws Exception {
                return throwable.getCause();
            }
        };
    }

    public static <T, S> Callable1<T, Option<S>> ignoringException(final Callable1<? super T, S> callable) {
        return handleException(callable, instanceOf(Exception.class));
    }

    public static <T, S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Class<? extends Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses).map(asInstanceOf()));
    }

    private static <T> Callable1<Class<? extends T>, Predicate<? super T>> asInstanceOf() {
        return new Callable1<Class<? extends T>, Predicate<? super T>>() {
            public Predicate<? super T> call(Class<? extends T> aClass) throws Exception {
                return instanceOf(aClass);
            }
        };
    }

    public static <T, S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Predicate<? super Exception> first) {
        return handleException(callable, sequence(first));
    }

    public static <T, S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Predicate<? super Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses));
    }

    public static <T, S> Callable1<T, Option<S>> handleException(final Callable1<? super T, S> callable, final Iterable<? extends Predicate<? super Exception>> predicates) {
        return new Callable1<T, Option<S>>() {
            public Option<S> call(T t) throws Exception {
                try {
                    return Option.some(callable.call(t));
                } catch (Exception e) {
                    if (sequence(predicates).exists(Predicates.<Exception>matches(e))) {
                        return none();
                    }
                    throw e;
                }
            }
        };
    }

    public static <T, S> Callable1<T, Either<S, Throwable>> captureException(final Callable1<? super T, S> callable) {
        return new Callable1<T, Either<S, Throwable>>() {
            public Either<S, Throwable> call(T input) throws Exception {
                try {
                    return left(callable.call(input));
                } catch (Throwable e) {
                    return right(e);
                }
            }
        };
    }

    public static Callable1<PrintWriter, Void> printStackTrace(final Throwable e) {
        return new Callable1<PrintWriter, Void>() {
            public Void call(PrintWriter writer) {
                e.printStackTrace(writer);
                return VOID;
            }
        };
    }
}
