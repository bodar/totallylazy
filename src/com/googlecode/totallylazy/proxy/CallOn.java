package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.proxy.Generics.getGenericSuperclassType;
import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public abstract class CallOn<T, S> implements Callable1<T,S>, InvocationHandler {
    protected T call;
    private Invocation invocation;

    protected CallOn() {
        Class<T> aClass = getGenericSuperclassType(this.getClass(), 0);
        call = createProxy(aClass, this);
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation = new Invocation(proxy, method, arguments);
        return null;
    }

    public S call(T t) throws Exception {
        return (S) invocation.call(t);
    }
}
