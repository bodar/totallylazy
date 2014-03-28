package com.googlecode.totallylazy;

public interface Functor<T> {
    <S> Functor<S> map(Function<? super T, ? extends S> callable);
}
