package com.googlecode.totallylazy;

public interface Extractor<A, B> {
    Iterable<B> extract(A a);
}
