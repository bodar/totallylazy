package com.googlecode.totallylazy;

import java.io.PrintWriter;

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

    public static Seq<Throwable> causes(Throwable throwable) {
        return iterate(getCause(), throwable).
                takeWhile(notNullValue());
    }

    public static Function<Throwable, Throwable> getCause() {
        return new Function<Throwable, Throwable>() {
            public Throwable call(final Throwable throwable) throws Exception {
                return throwable.getCause();
            }
        };
    }

    public static final Function<Exception,String> message = new Function<Exception, String>() {
        @Override
        public String call(Exception e) throws Exception {
            return e.getMessage();
        }
    };

    public static Function<Exception, String> message() {
        return message;
    }

    public static <T, S> Function<T, Option<S>> ignoringException(final Function<? super T, ? extends S> callable) {
        return optional(callable);
    }

    @SafeVarargs
    public static <T, S> Function<T, Option<S>> handleException(final Function<? super T, ? extends S> callable, final Class<? extends Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses).map(asInstanceOf()));
    }

    private static <T> Function<Class<? extends T>, Predicate<? super T>> asInstanceOf() {
        return new Function<Class<? extends T>, Predicate<? super T>>() {
            public Predicate<? super T> call(Class<? extends T> aClass) throws Exception {
                return instanceOf(aClass);
            }
        };
    }

    public static <T, S> Function<T, Option<S>> handleException(final Function<? super T, ? extends S> callable, final Predicate<? super Exception> first) {
        return handleException(callable, sequence(first));
    }

    @SafeVarargs
    public static <T, S> Function<T, Option<S>> handleException(final Function<? super T, ? extends S> callable, final Predicate<? super Exception>... exceptionClasses) {
        return handleException(callable, sequence(exceptionClasses));
    }

    public static <T, S> Function<T, Option<S>> handleException(final Function<? super T, ? extends S> callable, final Iterable<? extends Predicate<? super Exception>> predicates) {
        return new Function<T, Option<S>>() {
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

    public static <T, S> Function<T, Either<S, Throwable>> captureException(final Function<? super T, ? extends S> callable) {
        return new Function<T, Either<S, Throwable>>() {
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

    public static <A, B> Function<A, Either<Exception, B>> either(final Function<? super A, ? extends B> callable) {
        return new Function<A, Either<Exception, B>>() {
            @Override
            public Either<Exception, B> call(A a) throws Exception {
                try {
                    return Either.right(callable.call(a));
                } catch (Exception e) {
                    return Either.left(e);
                }
            }
        };
    }

    public static <A, B> Function<A, B> orElse(final Function<? super A, ? extends B> callable, final B result) {
        return new Function<A, B>() {
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


    public static <T, S> Function<T, Option<S>> optional(final Function<? super T, ? extends S> callable) {
        return new Function<T, Option<S>>() {
            public Option<S> call(T t) throws Exception {
                try {
                    return Option.option(callable.call(t));
                } catch (Exception e) {
                    return none();
                }
            }
        };
    }
}
