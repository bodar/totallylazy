package com.googlecode.totallylazy;

public interface Reducer<T, R> extends Function2<R, T, R>, Identity<R> {
}
