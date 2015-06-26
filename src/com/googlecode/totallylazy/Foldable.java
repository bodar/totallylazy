package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function2;

public interface Foldable<T> {
    <S> S fold(final S seed, final Function2<? super S, ? super T, ? extends S> callable);
}

