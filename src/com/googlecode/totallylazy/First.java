package com.googlecode.totallylazy;

public interface First<F> {
    F first();

    default F _1() { return first(); }
}
