package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.GenericType;
import com.googlecode.totallylazy.proxy.Generics;

import java.util.concurrent.Callable;

public abstract class GenericCallable<T> implements Callable<T>, GenericType<T>{
    private Class<T> type;

    protected GenericCallable() {
        type = Generics.getGenericSuperclassType(getClass(), 0);
    }

    public final Class<T> forClass() {
        return type;
    }
}
