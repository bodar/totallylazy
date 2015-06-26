package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generics {
    public static <T> Class<T> getGenericSuperclassType(Class<?> aClass, int index) {
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        return Unchecked.cast(actualTypeArguments[index]);
    }

    public static <T> Class<T> getGenericType(Class<?> aClass, int index) {
        ParameterizedType type = parameterizedType(aClass);
        Type[] actualTypeArguments = type.getActualTypeArguments();
        return Unchecked.cast(actualTypeArguments[index]);
    }

    public static ParameterizedType parameterizedType(Class<?> aClass) {
        return Sequences.sequence(aClass.getGenericInterfaces()).append(aClass.getGenericSuperclass()).safeCast(ParameterizedType.class).head();
    }


}
