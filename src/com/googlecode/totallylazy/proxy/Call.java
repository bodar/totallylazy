package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public class Call implements InvocationHandler{
    public static ThreadLocalInvocation invocation = new ThreadLocalInvocation();

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation.set(new Invocation(proxy, method, arguments));
        return null;
    }

    public static <T> T on(Class<T> aCLass){
         return createProxy(aCLass, new Call());
    }

    public static <T, S> Callable1<T,S> method(S value){
        return invocation.get();
    }
}
