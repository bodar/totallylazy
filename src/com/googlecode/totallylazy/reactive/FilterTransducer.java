package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.reactive.State.Continue;

public interface FilterTransducer<A> extends Transducer<A, A> {
    Predicate<? super A> predicate();

    static <A> FilterTransducer<A> filterTransducer(Predicate<? super A> predicate) { return () -> predicate;}

    @Override
    default Observer<A> apply(Observer<A> observer) {
        return Observer.observer(observer, item -> {
            if (!predicate().matches(item)) return Continue;
            return observer.next(item);
        });
    }
}