package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Methods;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Methods.methodName;
import static com.googlecode.totallylazy.Methods.parameterTypes;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.reflect.Modifier.isStatic;

public abstract class multi {
    public <T> T method(Object... args) {
        Method method = getClass().getEnclosingMethod();
        return matchMethod(isStatic(method.getModifiers()) ? null : enclosingInstance(this), method, args);
    }

    private static <T> T matchMethod(Object instance, Method method, Object[] args) {
        Method matched = sequence(method.getDeclaringClass().getDeclaredMethods()).
            remove(method).
            find(where(methodName(), is(method.getName())).
                    and(where(parameterTypes(), matches(sequence(args).map(toClass()))))).
            get();

        return Methods.invoke(matched, instance, args);
    }

    private static LogicalPredicate<Class<?>[]> matches(final Sequence<Class<?>> argumentClasses) {
        return new LogicalPredicate<Class<?>[]>() {
            @Override
            public boolean matches(Class<?>[] classes) {
                return sequence(classes).zip(argumentClasses).forAll(new LogicalPredicate<Pair<Class<?>, Class<?>>>() {
                    @Override
                    public boolean matches(Pair<Class<?>, Class<?>> pair) {
                        return pair.first().isAssignableFrom(pair.second());
                    }
                });
            }
        };
    }
}
