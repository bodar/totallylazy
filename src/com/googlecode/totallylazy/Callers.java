package com.googlecode.totallylazy;

import com.googlecode.totallylazy.concurrent.NamedExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callers {
    public static <T> Returns<T> callConcurrently(Callable<? extends T> callable) {
        ExecutorService service = executorService();
        try {
            return service.submit(callable)::get;
        } finally {
            service.shutdown();
        }
    }

    private static ExecutorService executorService() {
        return NamedExecutors.newCachedThreadPool(Callers.class);
    }

    @SafeVarargs
    public static <T> Sequence<T> callConcurrently(final Callable<? extends T>... callables) {
        return callConcurrently(sequence(callables));
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<? extends Callable<? extends T>> callables) {
        ExecutorService service = executorService();
        try {
            return callConcurrently(callables, service);
        } finally {
            service.shutdown();
        }
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<? extends Callable<? extends T>> callables, final Executor executor) {
        return Sequences.sequence(callables).<Callable<T>>unsafeCast().map(Callers.<T>asFutureTask()).
                map(Callers.<T>executeWith(executor)).
                realise().
                map(Callers.<T>realiseFuture());
    }

    public static <T> Function<FutureTask<T>, Future<T>> executeWith(final Executor executor) {
        return task -> {
            executor.execute(task);
            return task;
        };
    }

    public static <T> Function<Callable<T>, FutureTask<T>> asFutureTask() {
        return FutureTask::new;
    }

    public static <T> Function<Future<T>, T> realiseFuture() {
        return Future<T>::get;
    }

    public static <T> Function<Future<T>, T> realiseFuture(final long timeout, final TimeUnit unit) {
        return future -> future.get(timeout, unit);
    }

    public static <T> T call(final Callable<? extends T> callable) {
        return Functions.call(callable);
    }

    public static <A, B, C> C call(final Callable2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return Functions.call(callable, a, b);
    }

    public static <A, B, C, D> D call(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A a, final B b, final C c) {
        return Functions.call(callable, a, b, c);
    }

    public static <A, B, C, D, E> E call(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A a, final B b, final C c, final D d) {
        return Functions.call(callable, a, b, c, d);
    }

    public static <A, B, C, D, E, F> F call(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(callable, a, b, c, d, e);
    }
}