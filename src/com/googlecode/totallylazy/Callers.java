package com.googlecode.totallylazy;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callers {
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

    public static <T> Sequence<T> callConcurrently(final Callable<? extends T>... callables) {
        return callConcurrently(sequence(callables));
    }

    public static <T> Sequence<T> callConcurrently(final Iterable<? extends Callable<? extends T>> callables) {
        ExecutorService service = Executors.newCachedThreadPool();
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
        return new Function1<FutureTask<T>, Future<T>>() {
            public Future<T> call(FutureTask<T> task) throws Exception {
                executor.execute(task);
                return task;
            }
        };
    }

    public static <T> Function1<Callable<T>, FutureTask<T>> asFutureTask() {
        return new Function1<Callable<T>, FutureTask<T>>() {
            public FutureTask<T> call(Callable<T> callable) throws Exception {
                return new FutureTask<T>(callable);
            }
        };
    }

    public static <T> Function1<Future<T>, T> realiseFuture() {
        return new Function1<Future<T>, T>() {
            public final T call(final Future<T> future) throws Exception {
                return future.get();
            }
        };
    }

    public static <T> Function1<Future<T>, T> realiseFuture(final long timeout, final TimeUnit unit) {
        return new Function1<Future<T>, T>() {
            public final T call(final Future<T> future) throws Exception {
                return future.get(timeout, unit);
            }
        };
    }

    public static <T> T call(final Callable<? extends T> callable) {
        return Functions.call(callable);
    }

    public static <A, B> B call(final Callable1<? super A, ? extends B> callable, final A a) {
        return Functions.call(callable, a);
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

    public static <T> Function<T> asyncApply(Callable<T> callable) {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            final Future<T> future = service.submit(callable);
            return new Function<T>() {
                @Override
                public T call() throws Exception {
                    return future.get();
                }
            };
        } finally {
            service.shutdown();
        }
    }
}