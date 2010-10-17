package com.googlecode.totallylazy;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public final class Callers {
    public static <T> Sequence<T> callConcurrently(final Iterable<Callable<T>> callables) throws InterruptedException {
        return callConcurrently(sequence(callables).toList());
    }

    public static <T> Sequence<T> callConcurrently(final Callable<T>... callables) throws InterruptedException {
        return callConcurrently(asList(callables));
    }

    public static <T> Sequence<T> callConcurrently(final Collection<Callable<T>> callables) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(callables.size());
        Sequence<Future<T>> result = sequence(service.invokeAll(callables));
        service.shutdown();
        return result.map(Callables.<T>realise());
    }

    public static <T> T call(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (LazyException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public static <T, S> S call(final Callable1<? super T, S> callable, final T t) {
        try {
            return callable.call(t);
        } catch (LazyException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public static <T, S, R> R call(final Callable2<? super T, ? super S, R> callable, final T t, final S s) {
        try {
            return callable.call(t, s);
        } catch (LazyException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }
}