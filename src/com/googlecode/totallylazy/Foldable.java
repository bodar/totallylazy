package com.googlecode.totallylazy;

public interface Foldable<T> {
    <S> S fold(final S seed, final BiFunction<? super S, ? super T, ? extends S> callable);
}

