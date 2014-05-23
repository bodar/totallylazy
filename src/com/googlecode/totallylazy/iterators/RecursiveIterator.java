package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Sequences.isEmpty;
import static com.googlecode.totallylazy.Sequences.sequence;

public class RecursiveIterator<T> extends StatefulIterator<Sequence<T>> {
    private Sequence<T> rest;
    private final Function<? super Sequence<T>, Pair<? extends Iterable<? extends T>, ? extends Iterable<? extends T>>> callable;

    public RecursiveIterator(Iterable<? extends T> iterable, Function<? super Sequence<T>, Pair<? extends Iterable<? extends T>, ? extends Iterable<? extends T>>> callable) {
        rest(iterable);
        this.callable = callable;
    }

    private void rest(Iterable<? extends T> iterable) {this.rest = sequence(iterable);}

    @Override
    protected Sequence<T> getNext() throws Exception {
        if(isEmpty(rest)) return finished();
        Pair<? extends Iterable<? extends T>, ? extends Iterable<? extends T>> result = callable.apply(rest);
        rest(result.second());
        return Sequences.<T>sequence(result.first());
    }

}
