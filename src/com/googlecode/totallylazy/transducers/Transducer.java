package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.functions.Function1;

public interface Transducer<A,B> {
    Receiver<A> apply(Receiver<B> receiver);

    default <C> Transducer<A, C> map(Function1<? super B, ? extends C> mapper) {
        return Transducers.compose(this, Transducers.map(mapper));
    }
}

