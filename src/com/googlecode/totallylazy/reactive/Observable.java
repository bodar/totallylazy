package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;

public interface Observable<T> extends Filterable<T>, Functor<T> {
    AutoCloseable subscribe(Observer<T> observer);

    @SafeVarargs
    static <T> Observable<T> observable(T... values) {
        return observer -> {
            for (T value : values) observer.next(value);
            observer.complete();
            return () -> {};
        };
    }

    @Override
    default Observable<T> filter(Predicate<? super T> predicate){
        return observer -> Observable.this.subscribe(Observer.create(
                t -> { if (predicate.matches(t)) observer.next(t); }, observer));
    }

    @Override
    default <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return observer -> Observable.this.subscribe(Observer.create(
                t -> observer.next(mapper.apply(t)), observer));
    }

    default <R> Observable<R> flatMap(Function<? super T, ? extends Observable<R>> mapper) {
        return observer -> Observable.this.subscribe(Observer.create(
                t -> mapper.apply(t).subscribe(observer), observer));
    }
}