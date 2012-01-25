package com.googlecode.totallylazy;

// A Functor in Haskell
public interface Mappable<A, Self extends Mappable<?, Self>> {
    <B> Self map(Callable1<? super A, ? extends B> callable);
}
