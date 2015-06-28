package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.functions.Function0;
import com.googlecode.totallylazy.functions.Lazy;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.functions.Callables.toClass;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Proxy {
    private final static Map<Class<?>, Function0<Object>> cache = Collections.synchronizedMap(new HashMap<>());
    private final static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
    private final static Lazy<Constructor<?>> constructor = Lazy.lazy(() -> Object.class.getConstructor((Class[]) null));

    public static <T> T createProxy(Class<T> aCLass, InvocationHandler invocationHandler) {
        return createInstance(aCLass, invocationHandler);
    }

    public static <T> T createInstance(final Class<T> aClass, final Callback invocationHandler) {
        Callback[] callbacks = {invocationHandler, NoOp.INSTANCE};
        Function0<Object> instantiator = get(aClass, callbacks);
        Object instance = instantiator.apply();
        Factory factory = (Factory) instance;
        factory.setCallbacks(callbacks);
        return aClass.cast(instance);
    }

    private static Function0<Object> get(final Class<?> aClass, final Callback[] callbacks) {
        return cache.computeIfAbsent(aClass, c -> createInstantiator(c, callbacks));
    }

    private static Function0<Object> createInstantiator(Class<?> aClass, Callback[] callbacks) {
        IgnoreConstructorsEnhancer enhancer = new IgnoreConstructorsEnhancer();
        enhancer.setSuperclass(aClass);
        enhancer.setCallbackTypes(sequence(callbacks).map(toClass()).toArray(Class.class));
        enhancer.setCallbackFilter(new ToStringFilter());
        enhancer.setUseFactory(true);
        Class enhancedClass = enhancer.createClass();
        return () -> create(enhancedClass);
    }

    public static <T> T create(Class enhancedClass) throws ReflectiveOperationException {
        Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(enhancedClass, Proxy.constructor.value());
        return cast(constructor.newInstance());
    }
}