package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;

import java.util.function.Consumer;

public interface Observable<T> extends Filterable<T>, Functor<T> {
    AutoCloseable subscribe(Observer<T> observer);

    @SafeVarargs
    static <T> Observable<T> observable(T... values) {
        return observer -> {
            for (T value : values) observer.next(value);
            observer.complete();
            return () -> {
            };
        };
    }

    @Override
    default Observable<T> filter(Predicate<? super T> predicate) {
        return observable(observer -> t -> { if (predicate.matches(t)) observer.next(t); });
    }

    @Override
    default <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return observable(observer -> t -> observer.next(mapper.apply(t)));
    }

    default <R> Observable<R> flatMap(Function<? super T, ? extends Observable<R>> mapper) {
        return observable(observer -> t -> mapper.apply(t).subscribe(observer));
    }

    default <R> Observable<R> observable(Function<Observer<R>, Consumer<T>> function) {
        return observer -> Observable.this.subscribe(Observer.create(
                function.apply(observer), observer));
    }
}