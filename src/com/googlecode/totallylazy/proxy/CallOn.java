package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.proxy.Generics.getGenericSuperclassType;
import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public abstract class CallOn<T, S> implements Callable1<T,S>, InvocationHandler {
    private MethodInvocation<T,S> invocation;
    protected final T call, instance;

    protected CallOn() {
        Class<T> aClass = getGenericSuperclassType(this.getClass(), 0);
        call = instance = createProxy(aClass, this);
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation = new MethodInvocation<T,S>(method, arguments);
        return null;
    }

    public S call(T t) throws Exception {
        return invocation.call(t);
    }

    public MethodInvocation<T,S> invocation() {
        return invocation;
    }
}
