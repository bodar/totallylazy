package com.googlecode.totallylazy;

public interface CurriedReducer<T,R> extends CurriedFunction2<R, T, R>, Reducer<T,R> {
}
