package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import sun.java2d.SunGraphicsEnvironment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Invocation<T, R> implements Callable1<T, R> {
    private final Object proxy;
    private final Method method;
    private final Object[] arguments;

    public Invocation(Object proxy, Method method, Object[] arguments) {
        this.proxy = proxy;
        this.method = method;
        this.arguments = arguments;
    }

    public Object[] arguments() {
        return arguments;
    }

    public Method method() {
        return method;
    }

    public Object proxy() {
        return proxy;
    }

    @Override
    public String toString() {
        return method.getName() + sequence(arguments).toString("(", ",", ")");
    }

    @SuppressWarnings("unchecked")
    public R call(T instance) throws InvocationTargetException, IllegalAccessException {
        return (R) method.invoke(instance, arguments);
    }
}
