package com.googlecode.totallylazy;

public interface CurriedCombiner<T,R> extends CurriedReducer<T,R>, Combiner<T,R> {
}
