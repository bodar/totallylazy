package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyStub extends SubClassMe {
    protected InvocationHandler handler;

    @Override
    public int add(int a, int... b) throws Throwable {
        return (int) handler.invoke(this, Methods.method("superClass", "add", "Ldsahd"), new Object[]{a, b});
    }
}
