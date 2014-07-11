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
        return new Function1<Throwable, Throwable>() {
            public Throwable call(final Throwable throwable) throws Exception {
                return throwable.getCause();
            }
        };
    }

    public static final Function1<Exception,String> message = new Function1<Exception, String>() {
        @Override
        public String call(Exception e) throws Exception {
            return e.getMessage();
        }
    };

    public static Function1<Exception, String> message() {
        return message;
    }

    public static <T, S> Function1<T, Option<S>> ignoringException(final Callable1<? super T, ? extends S> callable) {
        return optional(callable);
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Callable1<? super T, ? extends S> callable, final Class<? extends Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses).map(asInstanceOf()));
    }

    private static <T> Function1<Class<? extends T>, Predicate<? super T>> asInstanceOf() {
        return new Function1<Class<? extends T>, Predicate<? super T>>() {
            public Predicate<? super T> call(Class<? extends T> aClass) throws Exception {
                return instanceOf(aClass);
            }
        };
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Callable1<? super T, ? extends S> callable, final Predicate<? super Exception> first) {
        return handleException(callable, sequence(first));
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Callable1<? super T, ? extends S> callable, final Predicate<? super Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses));
    }

    public static <T, S> Function1<T, Option<S>> handleException(final Callable1<? super T, ? extends S> callable, final Iterable<? extends Predicate<? super Exception>> predicates) {
        return new Function1<T, Option<S>>() {
            public Option<S> call(T t) throws Exception {
                try {
                    return Option.some(callable.call(t));
                } catch (Exception e) {
                    if (sequence(predicates).exists(matches(e))) {
                        return none();
                    }
                    throw e;
                }
            }
        };
    }

    public static String asString(Exception e) {
        StringPrintStream stream = new StringPrintStream();
        e.printStackTrace(stream);
        return stream.toString();
    }

    private static <T> Predicate<? super Predicate<? super T>> matches(final T instance) {
        return new Predicate<Predicate<? super T>>() {
            @Override
            public boolean matches(Predicate<? super T> other) {
                return other.matches(instance);
            }
        };
    }

    public static <T, S> Function1<T, Either<S, Throwable>> captureException(final Callable1<? super T, ? extends S> callable) {
        return new Function1<T, Either<S, Throwable>>() {
            public Either<S, Throwable> call(T input) throws Exception {
                try {
                    return left(callable.call(input));
                } catch (Throwable e) {
                    return right(e);
                }
            }
        };
    }

    public static Block<PrintWriter> printStackTrace(final Throwable e) {
        return new Block<PrintWriter>() {
            @Override
            protected void execute(PrintWriter writer) throws Exception {
                e.printStackTrace(writer);
            }
        };
    }

    public static <A, B> Function1<A, Either<Exception, B>> either(final Callable1<? super A, ? extends B> callable) {
        return new Function1<A, Either<Exception, B>>() {
            @Override
            public Either<Exception, B> call(final A a) throws Exception {
                return Either.either(new Callable<B>() {
                    @Override
                    public B call() throws Exception {
                        return callable.call(a);
                    }
                });
            }
        };
    }

    public static <A, B> Mapper<A, B> orElse(final Callable1<? super A, ? extends B> callable, final B result) {
        return new Mapper<A, B>() {
            @Override
            public B call(A a) throws Exception {
                try {
                    return callable.call(a);
                } catch (Exception e) {
                    return result;
                }
            }
        };
    }

    public static <T, S> Function1<T, Option<S>> optional(final Callable1<? super T, ? extends S> callable) {
        return new Function1<T, Option<S>>() {
            public Option<S> call(final T t) throws Exception {
                return Option.option(new Callable<S>() {
                    @Override
                    public S call() throws Exception {
                        return callable.call(t);
                    }
                });
            }
        };
    }
}
