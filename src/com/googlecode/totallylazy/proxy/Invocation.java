package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Functor;

public interface Invocation<T, R> extends Callable1<T, R>, Functor<R> {
}
