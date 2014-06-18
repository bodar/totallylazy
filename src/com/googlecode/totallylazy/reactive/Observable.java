package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Reducer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Predicates.countTo;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.whileTrue;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface Observable<T> extends Filterable<T>, Functor<T> {
    AutoCloseable subscribe(Observer<T> observer);

    @SafeVarargs
    static <T> Observable<T> observable(T... values) {
        return observable(sequence(values));
    }

    static <T> Observable<T> observable(Iterable<? extends T> values) {
        return observer -> {
            for (T value : values) observer.next(value);
            observer.complete();
            return () -> { };
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
        AtomicBoolean complete = new AtomicBoolean(false);
        return observable(observer -> t -> {
            if (complete.get()) return;
            if (predicate.matches(t)) observer.next(t);
            else {
                complete.set(true);
                observer.complete();
            }
        });
    }

    default Observable<T> drop(int count) {
        return dropWhile(countTo(count));
    }

    default Observable<T> dropWhile(Predicate<? super T> predicate) {
        return filter(not(whileTrue(predicate)));
    }

    default <R> Observable<R> observable(Function<Observer<R>, Block<T>> function) {
        return observer -> Observable.this.subscribe(Observer.create(
                function.apply(observer), observer));
    }
}