package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;

public interface Transducee<A,B> extends Function<Observer<B>, Observer<A>> {
    default <C> Transducee<A, C> map(final Transducee<B,C> transducee) {
        return Tranducees.compose(this, transducee);
    }

    default <C> Transducee<A, C> then(final Transducee<B,C> transducee) {
        return map(transducee);
    }
}

