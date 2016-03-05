package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;

public interface MapTransducer<A, B> extends Transducer<A, B> {
    Function<? super A, ? extends B> mapper();

    static <A, B> MapTransducer<A, B> mapTransducer(Function<? super A, ? extends B> mapper) {return () -> mapper;}

    @Override
    default Observer<A> apply(Observer<B> observer) {
        return Observer.observer(observer,
                item -> observer.next(mapper().apply(item)));
    }
}
