package com.googlecode.totallylazy;

// A Functor in Haskell
public interface Mappable<T, Self extends Mappable<?, Self>> {
    <S> Self map(Callable1<? super T, ? extends S> callable);
}
