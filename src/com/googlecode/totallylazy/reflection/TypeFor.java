package com.googlecode.totallylazy.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeFor<T>{
    private final Type type;

    protected TypeFor() {
        this.type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type get() {
        return type;
    }
}
