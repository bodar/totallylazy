package com.googlecode.totallylazy;

public interface Second<S> {
    S second();

    default S _2() { return second(); }
}
