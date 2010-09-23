package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.NoneIterator;

import java.util.Iterator;

public class None<T> extends Option<T>{
    public Iterator<T> iterator() {
        return new NoneIterator<T>();
    }
}
