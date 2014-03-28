package com.googlecode.totallylazy;

public interface Reducer<T, R> extends Callable2<R, T, R>, Identity<R> {
}
