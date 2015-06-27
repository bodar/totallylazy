package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.functions.Callables.returns;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.functions.Functions.function;
import static com.googlecode.totallylazy.predicates.Predicates.always;

public interface Atomic<T> extends Value<T> {
    Atomic<T> modify(Function1<? super T, ? extends T> callable);
    <R> R modifyReturn(Function1<? super T, ? extends Pair<? extends T, ? extends R>> callable);

    class constructors {
        public static <T> Atomic<T> atomic(final T t) {
            return atomic(t, returns(always(Integer.class)));
        }

        public static <T> Atomic<T> atomic(final T t, final Callable<? extends Predicate<? super Integer>> retryPredicate) {
            return new RetryingAtomic<T>(t, retryPredicate);
        }
    }

    static class RetryingAtomic<T> implements Atomic<T> {
        private final AtomicReference<T> reference;
        private final Callable<? extends Predicate<? super Integer>> retryPredicate;

        public RetryingAtomic(T t, Callable<? extends Predicate<? super Integer>> retryPredicate) {
            this.retryPredicate = retryPredicate;
            reference = new AtomicReference<T>(t);
        }

        @Override
        public Atomic<T> modify(Function1<? super T, ? extends T> callable) {
            return modifyReturn(function(callable).then(Pair.functions.<T, Atomic<T>>toPairWithSecond(this)));
        }

        @Override
        public <R> R modifyReturn(Function1<? super T, ? extends Pair<? extends T, ? extends R>> callable) {
            Predicate<? super Integer> retry = call(retryPredicate);
            for (int i = 0; retry.matches(i); i++) {
                T current = reference.get();
                Pair<? extends T, ? extends R> modified = call(callable, current);
                if (reference.compareAndSet(current, modified.first())) return modified.second() ;
            }
            throw new RejectedExecutionException(String.format("Atomic operation could not be applied due to %s", retry));
        }

        @Override
        public T value() {
            return reference.get();
        }
    }
}