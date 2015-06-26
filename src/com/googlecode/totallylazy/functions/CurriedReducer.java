package com.googlecode.totallylazy.functions;

public interface CurriedReducer<T,R> extends Curried2<R, T, R>, Reducer<T,R> {
}
