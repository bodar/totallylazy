package com.googlecode.totallylazy;

public interface Functor<T> {
    <S> Functor<S> map(Callable1<? super T, ? extends S> callable);
}
