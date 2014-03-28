package com.googlecode.totallylazy;

public interface Foldable<T> {
    <S> S fold(final S seed, final Function2<? super S, ? super T, ? extends S> callable);
}

