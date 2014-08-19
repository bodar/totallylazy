package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Dispatcher.dispatcher;
import static com.googlecode.totallylazy.Methods.methodName;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Reflection.enclosingInstance;
import static java.lang.reflect.Modifier.isStatic;

public abstract class multi {
    private final Dispatcher dispatcher;

    protected multi(Predicate<? super Method> predicate) {
        Method enclosing = enclosing();
        Object instance = instance(enclosing);
        Class<?> aClass = declaringClass(enclosing, instance);
        String name = enclosing.getName();

        this.dispatcher = Dispatcher.dispatcher(aClass, instance,
                and(where(methodName(), is(name)),
                        not(enclosing),
                        predicate));
    }

    protected multi() {
        this(Predicates.<Method, multimethod>where(Methods.annotation(multimethod.class), notNullValue()));
    }

    public <T> T method(Object... args) {
        return this.<T>methodOption(args).get();
    }

    public <T> Option<T> methodOption(Object... args) {
        return dispatcher.invokeOption(args);
    }

    private Method enclosing() {return getClass().getEnclosingMethod();}

    private Class<?> declaringClass(Method method, Object instance) {
        return isStatic(method.getModifiers()) ? method.getDeclaringClass() : instance.getClass();
    }

    private Object instance(Method method) {return isStatic(method.getModifiers()) ? null : enclosingInstance(this);}
}
