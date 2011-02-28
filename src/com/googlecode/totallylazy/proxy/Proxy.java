package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Proxy {
    private final static Map<Class, ObjectInstantiator> cache = Collections.synchronizedMap(new HashMap<Class, ObjectInstantiator>());
    private final static ObjenesisStd objenesis = new ObjenesisStd(false);

    public static <T> T createProxy(Class<T> aCLass, InvocationHandler invocationHandler) {
        return (T) new Proxy().createInstance(aCLass, invocationHandler);
    }

    public Object createInstance(final Class aClass, final Callback invocationHandler) {
        Callback[] callbacks = {invocationHandler, NoOp.INSTANCE};
        ObjectInstantiator instantiator = get(aClass, callbacks);
        Object instance = instantiator.newInstance();
        Factory factory = (Factory) instance;
        factory.setCallbacks(callbacks);
        return instance;
    }

    private ObjectInstantiator get(final Class aClass, final Callback[] callbacks) {
        synchronized (cache) {
            if (!cache.containsKey(aClass)) {
                cache.put(aClass, createInstantiator(aClass, callbacks));
            }
            return cache.get(aClass);
        }
    }

    private ObjectInstantiator createInstantiator(Class aClass, Callback[] callbacks) {
        IgnoreConstructorsEnhancer enhancer = new IgnoreConstructorsEnhancer();
        enhancer.setSuperclass(aClass);
        enhancer.setCallbackTypes(sequence(callbacks).map(toClass()).toArray(Class.class));
        enhancer.setCallbackFilter(new ToStringFilter());
        enhancer.setUseFactory(true);
        Class enhancedClass = enhancer.createClass();
        return objenesis.getInstantiatorOf(enhancedClass);
    }


}
