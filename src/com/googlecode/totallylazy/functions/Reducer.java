package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Identity;

public interface Reducer<T, R> extends Function2<R, T, R>, Identity<R> {
}
