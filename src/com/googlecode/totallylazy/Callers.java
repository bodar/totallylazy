package com.googlecode.totallylazy;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public class Callers {
    public static <T> Sequence<T> callConcurrently(Sequence<Callable<T>> callables) throws InterruptedException {
        return callConcurrently(callables.toList());
    }

    public static <T> Sequence<T> callConcurrently(Callable<T>... callables) throws InterruptedException {
        return callConcurrently(asList(callables));
    }

    public static <T> Sequence<T> callConcurrently(Collection<Callable<T>> callables) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(callables.size());
        Sequence<Future<T>> result = sequence(service.invokeAll(callables));
        service.shutdown();
        return result.map(Callables.<T>realise());
    }

    public static <T> T call(Callable<T> callable) {
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

    public static <T, S> S call(Callable1<? super T, S> callable, T t) {
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

    public static <T, S, R> R call(Callable2<T, S, R> callable, T t, S s) {
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
