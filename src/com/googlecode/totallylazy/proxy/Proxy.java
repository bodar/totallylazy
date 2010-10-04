package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

public class Proxy {
    public static <T> T createProxy(Class<T> aCLass, InvocationHandler invocationHandler) {
        Enhancer enhancer = new IgnoreConstructorsEnhancer();
        enhancer.setSuperclass(aCLass);
        enhancer.setCallback(invocationHandler);
        return (T) enhancer.create();
    }
}
