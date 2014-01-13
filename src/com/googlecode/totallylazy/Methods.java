package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.AbstractPredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Methods {
    public static Function<Method, String> methodName() {
        return Method::getName;
    }

    public static Function<Method,? extends Class<?>> returnType() {
        return Method::getReturnType;
    }

    public static Function<Method, Class<?>[]> parameterTypes() {
        return Method::getParameterTypes;
    }

    public static <T extends Annotation> Function<Method, T> annotation(final Class<T> annotationClass) {
        return method -> method.getAnnotation(annotationClass);
    }

    public static Function<Method, Type> genericReturnType() {
        return Method::getGenericReturnType;
    }

    public static Function<Method, Type[]> genericParameterTypes() {
        return Method::getGenericParameterTypes;
    }

    public static Predicate<Method> modifier(final int modifier) {
        return method -> (method.getModifiers() & modifier) != 0;
    }

    public static Function<Class<?>, Method> method(final String name, final Class<?>... parameters) {
        return aClass -> aClass.getMethod(name, parameters);
    }

    public static <T> Option<Method> method(T instance, final String name, final Class<?>... parameters) {
        return method(instance.getClass(), name, parameters);
    }

    public static Option<Method> method(Class<?> aClass, String name, final Class<?>... parameters) {
        return handleException(method(name, parameters), instanceOf(NoSuchMethodException.class)).apply(aClass);
    }

    public static Function<Class<?>, Iterable<Method>> methods() {
        return aClass -> sequence(aClass.getMethods());
    }

    public static Function<Class<?>, Iterable<Method>> declaredMethods() {
        return aClass -> sequence(aClass.getDeclaredMethods());
    }

    public static Sequence<Method> allMethods(Class<?> aClass) {
        return allClasses(aClass).flatMap(Methods.declaredMethods());
    }

    public static <T, R> R invoke(Method method, T instance, Object... arguments) {
        try {
            method.setAccessible(true);
            return Unchecked.cast(method.invoke(instance, arguments));
        } catch (IllegalAccessException e) {
            throw LazyException.lazyException(e);
        } catch (InvocationTargetException e) {
            throw LazyException.lazyException(e);
        }
    }

    public static <R> Function<Method, R> invokeOn(final Object instance, final Object... arguments) {
        return method -> Unchecked.cast(invoke(method, instance, arguments));
    }
}
