package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.proxy.Generics.getGenericSuperclassType;
import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public abstract class CallOn<T, S> implements Callable1<T,S>, InvocationHandler {
    private Invocation<T,S> invocation;
    protected final T call;

    protected CallOn() {
        Class<T> aClass = getGenericSuperclassType(this.getClass(), 0);
        call = createProxy(aClass, this);
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation = new Invocation<T,S>(proxy, method, arguments);
        return null;
    }

    public S call(T t) throws Exception {
        return invocation.call(t);
    }

    public Invocation<T,S> invocation() {
        return invocation;
    }
}
