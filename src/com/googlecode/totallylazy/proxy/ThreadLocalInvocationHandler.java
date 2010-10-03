package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class ThreadLocalInvocationHandler implements InvocationHandler {
    private ThreadLocalInvocations invocations;

    public ThreadLocalInvocationHandler(ThreadLocalInvocations invocations) {
        this.invocations = invocations;
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocations.add(new Invocation(proxy, method, arguments));
        return null;
    }
}
