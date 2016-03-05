package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.reactive.State.Continue;
import static com.googlecode.totallylazy.reactive.State.Stop;

public interface Tranducees {

    static <A, B, C> Transducee<A, C> compose(Transducee<A, B> a, Transducee<B, C> b) {
        return observer -> a.apply(b.apply(observer));
    }

    static <A, B> Transducee<A, B> map(Function<? super A, ? extends B> mapper) {
        return observer -> Observer.observer(observer,
                item -> observer.next(mapper.apply(item)));
    }

    static <A, B> Transducee<A, B> flatMap(Function<? super A, ? extends Observable<B>> mapper) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return observer -> Observer.observer(observer, a -> {
            mapper.apply(a).subscribe(Observer.observer(observer, b -> {
                        State state = observer.next(b);
                        if(state.equals(Stop)) complete.set(true);
                        return state;
                    }));
                    return complete.get() ? Stop : Continue;
                });
    }

    static <A> Transducee<A, A> filter(Predicate<? super A> predicate) {
        return observer -> Observer.observer(observer, item -> {
            if (!predicate.matches(item)) return Continue;
            return observer.next(item);
        });
    }

    static <A, B> Transducee<A, B> scan(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        AtomicReference<B> reference = new AtomicReference<>(seed);
        return observer -> Observer.observer(observer, item ->
                observer.next(reference.updateAndGet(s -> reducer.apply(s, item))));
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
        return observer -> Observer.observer(observer, item -> {
            reference.set(item);
            return Continue;
        }, () -> {
            T t = reference.get();
            if (t != null) {
                observer.next(t);
                observer.finish();
            }
        });
    }

    static <A> Transducee<A, A> take(int limit) {
        AtomicInteger count = new AtomicInteger();
        return observer -> Observer.observer(observer, () -> {
            observer.start();
            if (limit == 0) {
                observer.finish();
                return Stop;
            } else return Continue;
        }, item -> {
            int position = count.incrementAndGet();
            if (position == limit) {
                observer.next(item);
                observer.finish();
                return Stop;
            }
            if (position > limit) return Stop;
            return observer.next(item);
        });
    }

    static <A> Transducee<A, A> takeWhile(Predicate<? super A> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return observer -> Observer.observer(observer, item -> {
            if (complete.get()) return Stop;
            if (predicate.matches(item)) return observer.next(item);
            else {
                complete.set(true);
                observer.finish();
                return Stop;
            }
        });
    }
}
