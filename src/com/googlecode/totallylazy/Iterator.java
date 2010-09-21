package com.googlecode.totallylazy;

public abstract class Iterator<T> implements java.util.Iterator<T>{
    public void remove() {
        throw new UnsupportedOperationException();
    }

    
}
