package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.reactive.Next.next;

public interface Tranducees {

    static <A, B, C> Transducee<A, C> compose(Transducee<A, B> a, Transducee<B, C> b) {
        return observer -> a.apply(b.apply(observer));
    }

    static <A, B> Transducee<A, B> map(Function<? super A, ? extends B> mapper) {
        return observer -> state -> observer.step(state.map(mapper));
    }

    default <A> Transducee<A, A> flatMap(Function<? super A, ? extends Transducee<A, A>> mapper) {
        return flatten(map(mapper));
    }

    static <A> Transducee<A, A> flatten(Transducee<A, Transducee<A, A>> transducee) {
        return observer -> outerState -> transducee.apply(innerState -> {
            if(innerState instanceof Next) {
                innerState.value().apply(observer).step(outerState);
                return;
            }
            observer.step(cast(innerState));
        });
    }

    static <A> Transducee<A, A> filter(Predicate<? super A> predicate) {
        return observer -> state -> {
            if (state instanceof Next && !state.matches(predicate)) return;
            observer.step(state);
        };
    }

    static <A, B> Transducee<A, B> scan(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        AtomicReference<B> reference = new AtomicReference<>(seed);
        return observer -> state ->
                observer.step(state.map(t -> reference.updateAndGet(s -> reducer.apply(s, t))));
    }

    static <A, B> Transducee<A, B> scan(Reducer<B, ? super A> reducer) {
        return scan(reducer.identityElement(), reducer);
    }

    static <A, B> Transducee<A, B> reduce(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return compose(scan(seed, reducer), last());
    }

    static <A, B> Transducee<A, B> reduce(Reducer<B, ? super A> reducer) {
        return compose(scan(reducer), last());
    }

    static <T> Transducee<T, T> last() {
        final AtomicReference<T> reference = new AtomicReference<>();
        return observer -> state -> {
            if (state instanceof Next) {
                reference.set(state.value());
                return;
            }
            if (state instanceof Complete) {
                T t = reference.get();
                if (t != null) observer.step(next(t));
            }
            observer.step(state);
        };
    }

    static <A> Transducee<A, A> take(int limit) {
        AtomicInteger count = new AtomicInteger();
        return observer -> state -> {
            int position = count.getAndIncrement();
            if (position == limit) {
                observer.step(Complete.complete());
                return;
            }
            if (position > limit) return;
            observer.step(state);
        };
    }

    static <A> Transducee<A, A> takeWhile(Predicate<? super A> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return observer -> state -> {
            if (complete.get()) return;
            if (state.matches(predicate)) observer.step(state);
            else {
                complete.set(true);
                observer.step(Complete.complete());
            }
        };
    }
}
