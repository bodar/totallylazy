package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Exceptions.handleException;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Methods {
    public static <T extends Annotation> Callable1<? super Method, T> annotation(final Class<T> annotationClass) {
        return new Callable1<Method, T>() {
            public T call(Method method) throws Exception {
                return method.getAnnotation(annotationClass);
            }
        };
    }

    public static Callable1<? super Method, Type> genericReturnType() {
        return new Callable1<Method, Type>() {
            public Type call(Method method) throws Exception {
                return method.getGenericReturnType();
            }
        };
    }

    public static Callable1<? super Method, Type[]> genericParameterTypes() {
        return new Callable1<Method, Type[]>() {
            public Type[] call(Method method) throws Exception {
                return method.getGenericParameterTypes();
            }
        };
    }

    public static LogicalPredicate<Method> modifier(final int modifier) {
        return new LogicalPredicate<Method>() {
            public boolean matches(Method method) {
                return (method.getModifiers() & modifier) != 0;
            }
        };
    }

    public static Callable1<Class, Method> method(final String name, final Class<?>... parameters) {
        return new Callable1<Class, Method>() {
            public Method call(Class aClass) throws Exception {
                return aClass.getMethod(name, parameters);
            }
        };
    }

    public static <T> Option<Method> method(T instance, final String name, final Class<?>... parameters)  {
        return method(instance.getClass(), name, parameters);
    }

    public static Option<Method> method(Class aClass, String name, final Class<?>... parameters) {
        return call(handleException(method(name, parameters), instanceOf(NoSuchMethodException.class)), aClass);
    }

    public static Callable1<Class<?>, Iterable<Method>> methods() {
        return new Callable1<Class<?>, Iterable<Method>>() {
            public Iterable<Method> call(Class<?> aClass) throws Exception {
                return sequence(aClass.getMethods());
            }
        };
    }

    public static <T, R> R invoke(Method method, T instance, Object... arguments) {
        try {
            method.setAccessible(true);
            return (R) method.invoke(instance, arguments);
        } catch (IllegalAccessException e) {
            throw new LazyException(e);
        } catch (InvocationTargetException e) {
            throw new LazyException(e);
        }
    }


    public static <R> Callable1<? super Method,R> invokeOn(final Object instance, final Object... arguments) {
        return new Callable1<Method, R>() {
            public R call(Method method) throws Exception {
                return (R) invoke(method, instance, arguments);
            }
        };
    }
}