package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

public class Proxy {
    public static <T> T createProxy(Class<T> aCLass, InvocationHandler invocationHandler) {
        Enhancer enhancer = new IgnoreConstructorsEnhancer();
        enhancer.setSuperclass(aCLass);
        enhancer.setCallbacks(new Callback[]{invocationHandler, NoOp.INSTANCE});
        enhancer.setCallbackFilter(new ToStringFilter());
        return (T) enhancer.create();
    }

    private static class ToStringFilter implements CallbackFilter {
        public int accept(Method method) {
            return method.getName().equals("toString") ? 1 : 0;
        }
    }
}
