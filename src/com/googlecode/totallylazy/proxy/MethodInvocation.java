package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Sequences.sequence;

public class MethodInvocation<A, B> implements Function1<A, B>, Invocation<A, B> {
    private final Method method;
    private final Object[] arguments;

    public MethodInvocation(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public Object[] arguments() {
        return arguments;
    }

    public Method method() {
        return method;
    }

    @Override
    public String toString() {
        return method.getName() + sequence(arguments).toString("(", ",", ")");
    }

    @Override
    public B call(A instance) throws InvocationTargetException, IllegalAccessException {
        return Unchecked.cast(method.invoke(instance, arguments));
    }
}
