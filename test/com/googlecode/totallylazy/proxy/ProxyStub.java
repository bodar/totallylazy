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
    public void add(byte[] a, byte[] b) throws Throwable {
        Method add = Methods.method("superClass", "add", "Ldsahd");
        handler.invoke(this, add, new Object[]{a, b});
    }
}
