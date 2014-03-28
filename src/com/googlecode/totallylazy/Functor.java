package com.googlecode.totallylazy;

public interface Functor<T> {
    <S> Functor<S> map(Function1<? super T, ? extends S> callable);
}
