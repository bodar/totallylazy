package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;

public interface Invocation<T, R> extends Function<T, R>, Functor<R> {
}
