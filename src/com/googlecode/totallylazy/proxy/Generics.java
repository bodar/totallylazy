package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generics {
    public static <T> Class<T> getGenericSuperclassType(Class<?> aClass, int index) {
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        return Unchecked.cast(actualTypeArguments[index]);
    }
}
