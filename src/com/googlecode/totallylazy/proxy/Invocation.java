package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;

public interface Invocation<T, R> extends Function1<T, R>, Functor<R> {
}
