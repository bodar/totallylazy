package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class CallableProxy {
    public static ThreadLocalInvocations invocations = new ThreadLocalInvocations();

    public static <T> T on(Class<T> aCLass){
        Class proxyClass = createProxyClass(aCLass);
        Object instance = createInstance(proxyClass);
        wireUpHandler(instance);
        return (T) instance;
    }

    private static void wireUpHandler(Object instance) {
        Factory factory = (Factory) instance;
        factory.setCallbacks(new Callback[]{
             new ThreadLocalInvocationHandler(invocations)
        });
    }

    private static  Object createInstance(Class proxyClass) {
        Objenesis objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }

    private static <T> Class createProxyClass(Class<T> aCLass) {
        Enhancer enhancer = new IgnoreConstructorsEnhancer();
        enhancer.setSuperclass(aCLass);
        enhancer.setUseFactory(true);
        enhancer.setCallbackTypes(new Class[]{ThreadLocalInvocationHandler.class});
        return enhancer.createClass();
    }

    public static <T, S> Callable1<T,S> method(S value){
        return invocations.pop();
    }

    public static <T, S> Callable1<T,S> method(S value, Class<S> aClass){
        return method(value);
    }
}
