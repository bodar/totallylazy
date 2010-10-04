package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public abstract class Call<T, S> implements Callable1<T,S>, InvocationHandler {
    protected T method;
    private Invocation invocation;

    protected Call() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        Class<T> aClass = (Class<T>) actualTypeArguments[0];
        method = createProxy(aClass, this);
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        invocation = new Invocation(proxy, method, arguments);
        return null;
    }

    public S call(T t) throws Exception {
        return (S) invocation.call(t);
    }

    // ThreadLocal Version Below

    public static ThreadLocalInvocations invocations = new ThreadLocalInvocations();

    public static <T> T on(Class<T> aCLass){
         return createProxy(aCLass, new ThreadLocalInvocationHandler(invocations));
    }

    public static <T, S> Callable1<T,S> method(S value){
        return invocations.pop();
    }
}
