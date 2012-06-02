package com.googlecode.totallylazy;

public interface Functor<T, Self extends Functor<?, Self>> {
    <S> Self map(Callable1<? super T, ? extends S> callable);
}
