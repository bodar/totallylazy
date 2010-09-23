package com.googlecode.totallylazy;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Some<T> extends Option<T> {
    private final T t;

    public Some(T t) {
        this.t = t;
    }

    public Iterator<T> iterator() {
        return sequence(t).iterator();
    }
}
