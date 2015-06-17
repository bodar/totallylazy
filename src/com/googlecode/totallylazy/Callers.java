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
    public static <T> Function0<T> callConcurrently(Callable<? extends T> callable) {
        ExecutorService service = executorService();
        try {
            final Future<? extends T> future = service.submit(callable);
            return () -> future.get();
        } finally {
            service.shutdown();
        }
    }

    private static ExecutorService executorService() {
        return NamedExecutors.newCachedThreadPool(Callers.class);
    }

    public static <T> Sequence<T> callConcurrently(final Callable<? extends T> first, final Callable<? extends T> second) {
        return callConcurrently(sequence(first, second));
    }

    public static <T> Sequence<T> callConcurrently(final Callable<? extends T> first, final Callable<? extends T> second, final Callable<? extends T> third) {
        return callConcurrently(sequence(first, second, third));
    }

    public static <T> Sequence<T> callConcurrently(final Callable<? extends T> first, final Callable<? extends T> second, final Callable<? extends T> third, final Callable<? extends T> fourth) {
        return callConcurrently(sequence(first, second, third, fourth));
    }

    public static <T> Sequence<T> callConcurrently(final Callable<? extends T> first, final Callable<? extends T> second, final Callable<? extends T> third, final Callable<? extends T> fourth, final Callable<? extends T> fifth) {
        return callConcurrently(sequence(first, second, third, fourth, fifth));
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

    public static <T> Function1<FutureTask<T>, Future<T>> executeWith(final Executor executor) {
        return task -> {
            executor.execute(task);
            return task;
        };
    }

    public static <T> Function1<Callable<T>, FutureTask<T>> asFutureTask() {
        return callable -> new FutureTask<T>(callable);
    }

    public static <T> Function1<Future<T>, T> realiseFuture() {
        return future -> future.get();
    }

    public static <T> Function1<Future<T>, T> realiseFuture(final long timeout, final TimeUnit unit) {
        return future -> future.get(timeout, unit);
    }

    public static <T> T call(final Callable<? extends T> callable) {
        return Functions.call(callable);
    }

    public static <A, B> B call(final Function1<? super A, ? extends B> callable, final A a) {
        return Functions.call(callable, a);
    }

    public static <A, B, C> C call(final Function2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return Functions.call(callable, a, b);
    }

    public static <A, B, C, D> D call(final Function3<? super A, ? super B, ? super C, ? extends D> callable, final A a, final B b, final C c) {
        return Functions.call(callable, a, b, c);
    }

    public static <A, B, C, D, E> E call(final Function4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A a, final B b, final C c, final D d) {
        return Functions.call(callable, a, b, c, d);
    }

    public static <A, B, C, D, E, F> F call(final Function5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(callable, a, b, c, d, e);
    }
}