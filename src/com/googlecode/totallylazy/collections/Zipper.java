package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Value;

public interface Zipper<T> extends Value<T> {
    Zipper<T> next();
    Zipper<T> previous();
    Zipper<T> first();
    Zipper<T> last();
    boolean isFirst();
    boolean isLast();
    int index();
    Zipper<T> index(int index);
}
