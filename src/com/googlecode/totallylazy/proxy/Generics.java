package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.Sequences.sequence;

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
        Sequence<Type> interfaces = sequence(aClass.getGenericInterfaces());
        if(!aClass.isEnum()) interfaces = interfaces.cons(aClass.getGenericSuperclass());
        return interfaces.safeCast(ParameterizedType.class).head();
    }
}
