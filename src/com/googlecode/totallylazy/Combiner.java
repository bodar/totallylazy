package com.googlecode.totallylazy;

public interface Combiner<T> extends Reducer<T, T>, Associative<T> {
}
