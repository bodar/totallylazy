package com.googlecode.totallylazy;

import java.io.PrintWriter;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Right.right;
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

    public static Function1<Throwable, Throwable> getCause() {
        return Throwable::getCause;
    }

    public static final Function1<Exception,String> message = Exception::getMessage;

    public static Function1<Exception, String> message() {
        return message;
    }

    public static <T, S> Function1<T, Option<S>> ignoringException(final Function1<? super T, ? extends S> callable) {
        return optional(callable);
    }

    @SafeVarargs
    public static <T, S> Function1<T, Option<S>> handleException(final Function1<? super T, ? extends S> callable, final Class<? extends Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses).map(asInstanceOf()));
    }

    private static <T> Function1<Class<? extends T>, Predicate<? super T>> asInstanceOf() {
        return Predicates::instanceOf;
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Function1<? super T, ? extends S> callable, final Predicate<? super Exception> first) {
        return handleException(callable, sequence(first));
    }

    @SafeVarargs
    public static <T, S> Function1<T, Option<S>> handleException(final Function1<? super T, ? extends S> callable, final Predicate<? super Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses));
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Function1<? super T, ? extends S> callable, final Iterable<? extends Predicate<? super Exception>> predicates) {
        return t -> {
            try {
                return Option.some(callable.call(t));
            } catch (Exception e) {
                if (sequence(predicates).exists(matches(e))) {
                    return none();
                }
                throw e;
            }
        };
    }

    public static String asString(Exception e) {
        StringPrintStream stream = new StringPrintStream();
        e.printStackTrace(stream);
        return stream.toString();
    }

    private static <T> Predicate<? super Predicate<? super T>> matches(final T instance) {
        return other -> other.matches(instance);
    }

    public static <T, S> Function1<T, Either<S, Throwable>> captureException(final Function1<? super T, ? extends S> callable) {
        return input -> {
            try {
                return left(callable.call(input));
            } catch (Throwable e) {
                return right(e);
            }
        };
    }

    public static Block<PrintWriter> printStackTrace(final Throwable e) {
        return e::printStackTrace;
    }

    public static <A, B> Function1<A, Either<Exception, B>> either(final Function1<? super A, ? extends B> callable) {
        return a -> Either.either(() -> callable.call(a));
    }

    public static <A, B> Function1<A, B> orElse(final Function1<? super A, ? extends B> callable, final B result) {
        return a -> {
            try {
                return callable.call(a);
            } catch (Exception e) {
                return result;
            }
        };
    }

    public static <T, S> Function1<T, Option<S>> optional(final Function1<? super T, ? extends S> callable) {
        return t -> Option.option(() -> callable.call(t));
    }
}
