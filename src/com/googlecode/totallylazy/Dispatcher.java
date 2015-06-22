package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.reflection.Methods.methodName;
import static com.googlecode.totallylazy.reflection.Methods.parameterTypes;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.comparators.Comparators.by;
import static com.googlecode.totallylazy.numbers.Numbers.ascending;
import static com.googlecode.totallylazy.numbers.Numbers.minimum;
import static com.googlecode.totallylazy.numbers.Numbers.sum;

public class Dispatcher {
    private final Class<?> aClass;
    private final Object instance;
    private final Predicate<? super Method> predicate;
    private final ConcurrentMap<List<?>, Option<Method>> cache = new ConcurrentHashMap<List<?>, Option<Method>>();

    private Dispatcher(Class<?> aClass, Object instance, Predicate<? super Method> predicate) {
        this.aClass = aClass;
        this.instance = instance;
        this.predicate = predicate;
    }

    public static Dispatcher dispatcher(Class<?> aClass, String name) {
        return dispatcher(aClass, where(methodName(), is(name)));
    }

    public static Dispatcher dispatcher(Class<?> aClass, Predicate<? super Method> predicate) {
        return dispatcher(aClass, null, predicate);
    }

    public static Dispatcher dispatcher(Object instance, String name) {
        return dispatcher(instance, where(methodName(), is(name)));
    }

    public static Dispatcher dispatcher(Object instance, Predicate<? super Method> predicate) {
        return dispatcher(instance.getClass(), instance, predicate);
    }

    public static Dispatcher dispatcher(Class<?> aClass, Object instance, Predicate<? super Method> predicate) {
        return new Dispatcher(aClass, instance, predicate);
    }

    public <T> T invoke(Object... args) {
        return this.<T>invokeOption(args).get();
    }

    public <T> Option<T> invokeOption(Object... args) {
        final List<Class<?>> argumentClasses = sequence(args).map(toClass()).toList();
        return cache.computeIfAbsent(argumentClasses, key -> Methods.allMethods(aClass).
                filter(predicate).
                filter(where(parameterTypes(), matches(argumentClasses))).
                sort(by(distanceFrom(argumentClasses), ascending())).
                headOption()).map(Methods.<T>invokeOn(instance, args));
    }

    private static Function1<Method, Number> distanceFrom(final Iterable<Class<?>> argumentClasses) {
        return method -> distanceFrom(argumentClasses, sequence(method.getParameterTypes()));
    }

    static Number distanceFrom(Iterable<Class<?>> argumentClasses, Iterable<Class<?>> parameterTypes) {
        return sequence(argumentClasses).zip(parameterTypes).map(distanceBetween().pair()).reduce(sum);
    }

    private static CurriedFunction2<Class<?>, Class<?>, Number> distanceBetween() {
        return Dispatcher::distanceBetween;
    }

    static Number distanceBetween(Class<?> argument, Class<?> parameterType) {
        if (argument.equals(parameterType)) return 0;
        return Numbers.add(parameterType.isInterface() ? 1 : 1.1, sequence(argument.getInterfaces()).
                append(argument.getSuperclass()).
                filter(not(nullValue())).
                map(distanceBetween().flip().apply(parameterType)).
                reduce(minimum));
    }

    private static LogicalPredicate<Class<?>[]> matches(final Iterable<Class<?>> argumentClasses) {
        return new LogicalPredicate<Class<?>[]>() {
            @Override
            public boolean matches(Class<?>[] classes) {
                return sequence(classes).equals(argumentClasses, new LogicalPredicate<Pair<Class<?>, Class<?>>>() {
                    @Override
                    public boolean matches(Pair<Class<?>, Class<?>> pair) {
                        return pair.first().isAssignableFrom(pair.second());
                    }
                });
            }
        };
    }
}

