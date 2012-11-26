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
    public static Function1<Method, String> methodName() {
        return new Function1<Method, String>() {
            @Override
            public String call(Method method) throws Exception {
                return method.getName();
            }
        };
    }

    public static Function1<Method,? extends Class<?>> returnType() {
        return new Function1<Method, Class<?>>() {
            @Override
            public Class<?> call(Method method) throws Exception {
                return method.getReturnType();
            }
        };
    }

    public static Function1<Method, Class<?>[]> parameterTypes() {
        return new Function1<Method, Class<?>[]>() {
            public Class<?>[] call(Method method) throws Exception {
                return method.getParameterTypes();
            }
        };
    }

    public static <T extends Annotation> Function1<Method, T> annotation(final Class<T> annotationClass) {
        return new Function1<Method, T>() {
            public T call(Method method) throws Exception {
                return method.getAnnotation(annotationClass);
            }
        };
    }

    public static Function1<Method, Type> genericReturnType() {
        return new Function1<Method, Type>() {
            public Type call(Method method) throws Exception {
                return method.getGenericReturnType();
            }
        };
    }

    public static Function1<Method, Type[]> genericParameterTypes() {
        return new Function1<Method, Type[]>() {
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

    public static Function1<Class<?>, Method> method(final String name, final Class<?>... parameters) {
        return new Function1<Class<?>, Method>() {
            public Method call(Class<?> aClass) throws Exception {
                return aClass.getMethod(name, parameters);
            }
        };
    }

    public static <T> Option<Method> method(T instance, final String name, final Class<?>... parameters) {
        return method(instance.getClass(), name, parameters);
    }

    public static Option<Method> method(Class<?> aClass, String name, final Class<?>... parameters) {
        return call(handleException(method(name, parameters), instanceOf(NoSuchMethodException.class)), aClass);
    }

    public static Function1<Class<?>, Iterable<Method>> methods() {
        return new Function1<Class<?>, Iterable<Method>>() {
            public Iterable<Method> call(Class<?> aClass) throws Exception {
                return sequence(aClass.getMethods());
            }
        };
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

    public static <R> Function1<Method, R> invokeOn(final Object instance, final Object... arguments) {
        return new Function1<Method, R>() {
            public R call(Method method) throws Exception {
                return Unchecked.cast(invoke(method, instance, arguments));
            }
        };
    }
}
