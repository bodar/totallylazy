package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public final class Callers {
    public static <T> Sequence<T> callConcurrently(final Callable<T>... callables) {
        return callConcurrently(sequence(callables));
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<Callable<T>> callables) {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            return callConcurrently(callables, service);
        } finally {
            service.shutdown();
        }
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<Callable<T>> callables, final Executor executor) {
        return sequence(callables).map(Callers.<T>asFutureTask()).
                map(Callers.<T>executeWith(executor)).
                realise().
                map(Callers.<T>realiseFuture());
    }

    public static <T> Callable1<? super FutureTask<T>, Future<T>> executeWith(final Executor executor) {
        return new Callable1<FutureTask<T>, Future<T>>() {
            public Future<T> call(FutureTask<T> task) throws Exception {
                executor.execute(task);
                return task;
            }
        };
    }

    public static <T> Callable1<? super Callable<T>, FutureTask<T>> asFutureTask() {
        return new Callable1<Callable<T>, FutureTask<T>>() {
            public FutureTask<T> call(Callable<T> callable) throws Exception {
                return new FutureTask<T>(callable);
            }
        };
    }

    public static <T> Callable1<? super Future<T>, T> realiseFuture() {
        return new Callable1<Future<T>, T>() {
            public final T call(final Future<T> future) throws Exception {
                return future.get();
            }
        };
    }

    public static <T> Callable1<? super Future<T>, T> realiseFuture(final long timeout, final TimeUnit unit) {
        return new Callable1<Future<T>, T>() {
            public final T call(final Future<T> future) throws Exception {
                return future.get(timeout, unit);
            }
        };
    }

    public static <T> T call(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public static <T, S> S call(final Callable1<? super T, S> callable, final T t) {
        try {
            return callable.call(t);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public static <T, S, R> R call(final Callable2<? super T, ? super S, R> callable, final T t, final S s) {
        try {
            return callable.call(t, s);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }
}