package com.googlecode.totallylazy.iterators;

public abstract class NullTerminatingIterator<T> extends StatefulIterator<T> {
    protected abstract T nextNullTerminating() throws Exception;

    @Override
    protected T getNext() throws Exception {
        T t = nextNullTerminating();
        if (t == null) {
            return finished();
        }
        return t;
    }
}
