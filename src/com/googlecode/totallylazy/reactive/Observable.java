package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Predicates.countTo;
import static com.googlecode.totallylazy.Predicates.whileFalse;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.reactive.Next.next;

public interface Observable<T> extends Filterable<T>, Functor<T> {

    AutoCloseable EMPTY_CLOSEABLE = () -> {
    };

    AutoCloseable subscribe(Observer<T> observer);


    @SafeVarargs
    static <T> Observable<T> observable(T... values) {
        return observable(sequence(values));
    }

    static <T> Observable<T> observable(Iterable<? extends T> values) {
        return observer -> {
            for (T value : values) observer.step(next(value));
            observer.step(Complete.complete());
            return EMPTY_CLOSEABLE;
        };
    }

    @Override
    default Observable<T> filter(Predicate<? super T> predicate) {
        return observable(observer -> state -> {
            if (state instanceof Next && !state.matches(predicate)) return;
            observer.step(state);
        });
    }

    @Override
    default <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return observable(observer -> state -> observer.step(state.map(mapper)));
    }

    default <R> Observable<R> flatMap(Function<? super T, ? extends Observable<R>> mapper) {
        return observable(observer -> state -> {
            if (state instanceof Next) {
                state.map(mapper).value().subscribe(observer);
                return;
            }
            observer.step(cast(state));
        });
    }

    default <S> Observable<S> scan(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        AtomicReference<S> reference = new AtomicReference<>(seed);
        return observable(observer -> state ->
                observer.step(state.map(t -> reference.updateAndGet(s -> reducer.apply(s, t)))));
    }

    default <S> Observable<S> scan(Reducer<? super T, S> reducer) {
        return scan(reducer.identityElement(), reducer);
    }

    default Observable<T> last() {
        return observer -> Observable.this.subscribe(new LastObserver<>(observer));
    }

    default <S> Observable<S> reduce(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        return scan(seed, reducer).last();
    }

    default <S> Observable<S> reduce(Reducer<? super T, S> reducer) {
        return scan(reducer).last();
    }

    default Observable<T> take(int count) {
        return takeWhile(countTo(count));
    }

    default Observable<T> takeWhile(Predicate<? super T> predicate) {
        AtomicBoolean complete = new AtomicBoolean(false);
        return observable(observer -> state -> {
            if (complete.get()) return;
            if (state.matches(predicate)) observer.step(state);
            else {
                complete.set(true);
                observer.step(Complete.complete());
            }
        });
    }

    default Observable<T> drop(int count) {
        return dropWhile(countTo(count));
    }

    default Observable<T> dropWhile(Predicate<? super T> predicate) {
        return filter(whileFalse(predicate));
    }

    default <K> Observable<Group<K, T>> groupBy(Function<? super T, ? extends K> keyExtractor) {
        return observer -> Observable.this.subscribe(new GroupObserver<>(keyExtractor, observer));
    }

    default <R> Observable<R> observable(Function<? super Observer<R>, Block<State<T>>> function) {
        return observer -> Observable.this.subscribe(state -> function.apply(observer).apply(state));
    }

    default Observable<List<T>> toList() {
        return reduce(list(), (l, t) -> {
            l.add(t);
            return l;
        });
    }

    default Observable<Sequence<T>> toSequence() {
        return toList().map(Sequences::<T>sequence);
    }
}