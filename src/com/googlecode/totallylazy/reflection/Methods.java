package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Methods {
    public static Function1<Method, String> methodName() {
        return Method::getName;
    }

    public static Function1<Method,? extends Class<?>> returnType() {
        return Method::getReturnType;
    }

    public static Function1<Method, Class<?>[]> parameterTypes() {
        return Method::getParameterTypes;
    }

    public static <T extends Annotation> Function1<Method, T> annotation(final Class<T> annotationClass) {
        return method -> method.getAnnotation(annotationClass);
    }

    public static Function1<Method, Type> genericReturnType() {
        return Method::getGenericReturnType;
    }

    public static Function1<Method, Type[]> genericParameterTypes() {
        return Method::getGenericParameterTypes;
    }

    public static LogicalPredicate<Method> modifier(final int modifier) {
        return new LogicalPredicate<Method>() {
            public boolean matches(Method method) {
                return (method.getModifiers() & modifier) != 0;
            }
        };
    }

    public static Function1<Class<?>, Method> method(final String name, final Class<?>... parameters) {
        return aClass -> aClass.getMethod(name, parameters);
    }

    public static <T> Option<Method> method(T instance, final String name, final Class<?>... parameters) {
        return method(instance.getClass(), name, parameters);
    }

    public static Option<Method> method(Class<?> aClass, String name, final Class<?>... parameters) {
        return call(handleException(method(name, parameters), instanceOf(NoSuchMethodException.class)), aClass);
    }

    public static Function1<Class<?>, Iterable<Method>> methods() {
        return aClass -> sequence(aClass.getMethods());
    }

    public static Function1<Class<?>, Iterable<Method>> declaredMethods() {
        return aClass -> sequence(aClass.getDeclaredMethods());
    }

    public static Sequence<Method> allMethods(Class<?> aClass) {
        return allClasses(aClass).flatMap(Methods.declaredMethods());
    }

    public static <T, R> R invoke(Method method, T instance, Object... arguments) {
        try {
            method.setAccessible(true);
            return Unchecked.cast(method.invoke(instance, arguments));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw LazyException.lazyException(e);
        }
    }

    public static <R> Function1<Method, R> invokeOn(final Object instance, final Object... arguments) {
        return method -> Unchecked.cast(invoke(method, instance, arguments));
    }
}
