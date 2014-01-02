package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Value;

public interface Zipper<T> extends Value<T> {
    boolean isFirst();

    boolean isLast();

    Zipper<T> first();

    Zipper<T> last();

    Zipper<T> next();

    Zipper<T> previous();

    Option<? extends Zipper<T>> nextOption();

    Option<? extends Zipper<T>> previousOption();

    int index();

    Zipper<T> index(int index);
}
