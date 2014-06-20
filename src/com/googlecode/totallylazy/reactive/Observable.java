package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Predicates.countTo;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.whileFalse;
import static com.googlecode.totallylazy.Predicates.whileTrue;
import static com.googlecode.totallylazy.Sequences.sequence;

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
            for (T value : values) observer.next(value);
            observer.complete();
            return EMPTY_CLOSEABLE;
        };
    }

    @Override
    default Observable<T> filter(Predicate<? super T> predicate) {
        return observable(observer -> t -> {
            if (predicate.matches(t)) observer.next(t);
        });
    }

    @Override
    default <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return observable(observer -> t -> observer.next(mapper.apply(t)));
    }

    default <R> Observable<R> flatMap(Function<? super T, ? extends Observable<R>> mapper) {
        return observable(observer -> t -> mapper.apply(t).subscribe(observer));
    }

    default <S> Observable<S> scan(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        AtomicReference<S> reference = new AtomicReference<>(seed);
        return observable(observer -> t -> observer.next(reference.updateAndGet(s -> reducer.apply(s, t))));
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
        return filter(whileTrue(predicate));
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

    default <R> Observable<R> observable(Function<? super Observer<R>, Block<T>> function) {
        return observer -> Observable.this.subscribe(Observer.observer(
                function.apply(observer), observer));
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