package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;

public interface Transducer<A,B> {
    Observer<A> apply(Observer<B> observer);

    default <C> Transducer<A, C> map(Function<? super B, ? extends C> mapper) {
        return Transducers.compose(this, Transducers.map(mapper));
    }
}

