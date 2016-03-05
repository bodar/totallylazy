package com.googlecode.totallylazy;

import com.googlecode.totallylazy.reactive.Transducee;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public interface Reducible<A> {
    <S> S reduce(S seed, Function2<? super S, ? super A, ? extends S> callable);

    default <S> S reduce(Reducer<S, ? super A> callable) {
        return reduce(callable.identityElement(), callable);
    }

    default <B> Reducible<B> map(Function<? super A, ? extends B> mapper) {
        return reducible(Transducers.map(mapper));
    }

    default Reducible<A> filter(Predicate<? super A> predicate) {
        return reducible(Transducers.filter(predicate));
    }

    default <B> Reducible<B> flatMap(Function<? super A, ? extends Reducible<B>> mapper) {
        return flatten(map(mapper));
    }

    static <T> Reducible<T> flatten(Reducible<Reducible<T>> nested) {
        return new Reducible<T>() {
            @Override
            public <S> S reduce(S seed, Function2<? super S, ? super T, ? extends S> callable) {
                return nested.reduce(seed, (s, reducible) -> reducible.reduce(seed, callable::apply));
            }
        };
    }

    default Reducible<A> take(int count) {
        return reducible(Transducers.take(count));
    }

    default Reducible<A> takeWhile(Predicate<? super A> predicate) {
        return reducible(Transducers.takeWhile(predicate));
    }

    default <B, S> Reducible<B> reducible(Transducer<A, B, S> transformer) {
        return new Reducible<B>() {
            @Override
            public <R> R reduce(R seed, Function2<? super R, ? super B, ? extends R> reducer) {
                return Reducible.this.reduce(seed, Unchecked.<Function<Function2, Function2<R, A, R>>>cast(transformer).apply(reducer));
            }
        };
    }
}

interface Transducer<A, B, S> extends Function<Function2<S, B, S>, Function2<S, A, S>> {


}

interface Transducers {
    static <A, B, S> Transducer<A, B, S> map(Function<? super A, ? extends B> mapper) {
        return reducer ->
                (seed, input) ->
                        reducer.apply(seed, mapper.apply(input));
    }

    static <A, S> Transducer<A, A, S> filter(Predicate<? super A> predicate) {
        return reducer ->
                (seed, input) ->
                        predicate.matches(input) ? reducer.apply(seed, input) : seed;
    }

    static <A, S> Transducer<A, A, S> take(int limit) {
        AtomicInteger count = new AtomicInteger();
        return reducer ->
                (seed, input) ->
                        count.incrementAndGet() > limit ? seed : reducer.apply(seed, input);
    }

    static <A, S> Transducer<A, A, S> takeWhile(Predicate<? super A> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return reducer ->
                (seed, input) -> {
                    if (complete.get()) return seed;
                    if (predicate.matches(input)) return reducer.apply(seed, input);
                    complete.set(true);
                    return seed;
                };

    }
}
