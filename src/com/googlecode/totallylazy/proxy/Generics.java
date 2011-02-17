package com.googlecode.totallylazy.proxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generics {
    public static <T> Class<T> getGenericSuperclassType(Class aClass, int index) {
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[index];
    }
}
