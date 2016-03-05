package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.reactive.State.Continue;
import static com.googlecode.totallylazy.reactive.State.Stop;

public interface Transducers {
    static <A> Transducer<A, A> identity() { return observer -> observer; }
    static <A> Transducer<A, A> identity(Class<A> aClass) { return observer -> observer; }

    static <A, B, C> Transducer<A, C> compose(Transducer<A, B> a, Transducer<B, C> b) {
        return CompositeTransducer.compositeTransducer(a, b);
    }

    static <A, B> Transducer<A, B> map(Function<? super A, ? extends B> mapper) {
        return MapTransducer.mapTransducer(mapper);
    }

    static <A, B> Transducer<A, B> flatMap(Function<? super A, ? extends Observable<B>> mapper) {
        return FlatMapTransducer.flatMapTransducer(mapper);
    }

    static <A> Transducer<A, A> filter(Predicate<? super A> predicate) {
        return FilterTransducer.filterTransducer(predicate);
    }

    static <A, B> Transducer<A, B> scan(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return ScanTransducer.scanTransducer(seed, reducer);
    }

    static <A, B> Transducer<A, B> scan(Reducer<B, ? super A> reducer) {
        return ScanTransducer.scanTransducer(reducer);
    }

    static <A, B> Transducer<A, B> reduce(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return compose(scan(seed, reducer), last());
    }

    static <A, B> Transducer<A, B> reduce(Reducer<B, ? super A> reducer) {
        return compose(scan(reducer), last());
    }

    static <T> Transducer<T, T> last() {
        return LastTransducer.lastTransducer();
    }

    static <A> Transducer<A, A> take(int limit) {
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

    static <A> Transducer<A, A> takeWhile(Predicate<? super A> predicate) {
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

    static <A> Transducer<A, A> drop(int limit) {
        AtomicInteger count = new AtomicInteger();
        if (limit == 0) return Transducers.identity();
        return observer -> Observer.observer(observer, item -> {
            int position = count.getAndIncrement();
            if (position < limit) {
                return Continue;
            }
            return observer.next(item);
        });
    }


    static <T, K> Transducer<T,Group<K, T>> groupBy(Function<? super T, ? extends K> keyExtractor) {
        return GroupByTransducer.groupByTransducer(keyExtractor);
    }

}
