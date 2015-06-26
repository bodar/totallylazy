package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;

public interface Functor<T> {
    <S> Functor<S> map(Function1<? super T, ? extends S> callable);
}
