package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyStub extends SubClassMe {
    protected InvocationHandler handler;

    @Override
    public void add(byte[] a, byte[] b) throws Throwable {
        handler.invoke(this, Methods.method("superClass", "add", "Ldsahd"), new Object[]{a, b});
    }
}
