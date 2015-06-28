package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyStub extends SubClassMe {
    private final InvocationHandler handler;

    public ProxyStub(InvocationHandler handler) {
        this.handler = handler;
    }

    @Override
    public int add(int a, int b) throws Throwable {
        Method add = Methods.method(getClass(), "add", "Ldsahd");
        return (int) handler.invoke(this, add, new Object[]{a, b});
    }
}
