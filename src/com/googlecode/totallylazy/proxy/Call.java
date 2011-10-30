package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

@SuppressWarnings("unchecked")
public class Call<T,R> implements InvocationHandler{
    public static ThreadLocalInvocation invocation = new ThreadLocalInvocation();

    private Call() {}

    public static <T> T on(Class<T> aCLass){
         return createProxy(aCLass, new Call());
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation.set(new Invocation<T,R>(proxy, method, arguments));
        return null;
    }

    public static <T, S> Invocation<T, S> method(S value){
        return invocation.get();
    }
}
