package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public final class Callers {
    public static <T> Sequence<T> callConcurrently(final Iterable<Callable<T>> callables) {
        return callConcurrently(sequence(callables).toList());
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<Callable<T>> callables, final Executor executor) {
        return callConcurrently(sequence(callables).toList(), executor);
    }

    public static <T> Sequence<T> callConcurrently(final Callable<T>... callables) {
        return callConcurrently(asList(callables));
    }

    public static <T> Sequence<T> callConcurrently(final Collection<Callable<T>> callables) {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            return callConcurrently(callables, service);
        } finally {
            service.shutdown();
        }
    }

    private static <T> Sequence<T> callConcurrently(final Collection<Callable<T>> callables, final Executor service) {
        List<Future<T>> futures = new ArrayList<Future<T>>();
        for (Callable<T> callable : callables) {
            FutureTask<T> future = new FutureTask<T>(callable);
            futures.add(future);
            service.execute(future);
        }
        return sequence(futures).map(Callables.<T>realiseFuture());
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