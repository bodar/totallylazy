package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Functions.identity;
import static com.googlecode.totallylazy.Predicates.whileFalse;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.reactive.State.Stop;

public interface Observable<T> extends Filterable<T>, Functor<T> {

    AutoCloseable EMPTY_CLOSEABLE = () -> {
    };

    AutoCloseable subscribe(Observer<T> observer);


    @SafeVarargs
    static <T> Observable<T> observable(T... values) {
        return observable(sequence(values));
    }

    static <T> Observable<T> observable(Iterable<? extends T> values) {
        return observable(values.iterator());
    }

    static <T> Observable<T> observable(Iterator<? extends T> iterator) {
        return observer -> {
            if (observer.start().equals(Stop)) return EMPTY_CLOSEABLE;
            while (iterator.hasNext()) {
                T value = iterator.next();
                if (observer.next(value).equals(Stop)) break;
            }
            observer.finish();
            return EMPTY_CLOSEABLE;
        };
    }

    @Override
    default Observable<T> filter(Predicate<? super T> predicate) {
        return transduce(Transducers.filter(predicate));
    }

    @Override
    default <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return transduce(Transducers.map(mapper));
    }

    default <R> Observable<R> flatMap(Function<? super T, ? extends Observable<R>> mapper) {
        return transduce(Transducers.flatMap(mapper));
    }

    static <R> Observable<R> flatten(Observable<Observable<R>> nested) {
        return nested.flatMap(identity());
    }

    default <S> Observable<S> scan(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        return transduce(Transducers.scan(seed, reducer));
    }

    default <S> Observable<S> scan(Reducer<S, ? super T> reducer) {
        return transduce(Transducers.scan(reducer));
    }

    default Observable<T> last() {
        return transduce(Transducers.last());
    }

    default <S> Observable<S> reduce(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        return transduce(Transducers.reduce(seed, reducer));
    }

    default <S> Observable<S> reduce(Reducer<S, ? super T> reducer) {
        return transduce(Transducers.reduce(reducer));
    }

    default Observable<T> take(int limit) {
        return transduce(Transducers.take(limit));
    }

    default Observable<T> takeWhile(Predicate<? super T> predicate) {
        return transduce(Transducers.takeWhile(predicate));
    }

    default Observable<T> drop(int count) {
        return transduce(Transducers.drop(count));
    }

    default Observable<T> dropWhile(Predicate<? super T> predicate) {
        return filter(whileFalse(predicate));
    }

    default <K> Observable<Group<K, T>> groupBy(Function<? super T, ? extends K> keyExtractor) {
        return transduce(Transducers.groupBy(keyExtractor));
    }

    default <R> Observable<R> transduce(Transducer<T, R> transducer) {
        return transduce(this, transducer);
    }

    static <T, R> Observable<R> transduce(Observable<T> observable, Transducer<T, R> transducer) {
        return observer -> observable.subscribe(transducer.apply(observer));
    }

    default Observable<List<T>> toList() {
        return reduce(Lists.functions.add());
    }

    default Observable<Sequence<T>> toSequence() {
        return toList().map(Sequences::<T>sequence);
    }
}