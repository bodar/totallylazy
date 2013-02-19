package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Methods.allMethods;
import static com.googlecode.totallylazy.Methods.methodName;
import static com.googlecode.totallylazy.Methods.parameterTypes;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.comparators.Comparators.by;
import static com.googlecode.totallylazy.numbers.Numbers.minimum;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static java.lang.reflect.Modifier.isStatic;

public abstract class multi {
    private final Predicate<Method> predicate;

    protected multi(Predicate<Method> predicate) {
        this.predicate = predicate;
    }

    protected multi() {
        this(Predicates.<Method, multimethod>where(Methods.annotation(multimethod.class), notNullValue()));
    }

    public <T> T method(Object... args) {
        return this.<T>methodOption(args).get();
    }

    public <T> Option<T> methodOption(Object... args) {
        Method method = getClass().getEnclosingMethod();
        Sequence<Class<?>> argumentClasses = sequence(args).map(toClass());
        Object instance = instance(method);
        Class<?> aClass = declaringClass(method, instance);
        Sequence<Method> methods = allMethods(aClass);
        return methods.
                filter(where(methodName(), is(method.getName()))).
                filter(predicate).
                filter(where(parameterTypes(), matches(argumentClasses))).
                filter(where(parameterTypes(), not(exactMatch(sequence(method.getParameterTypes()))))).
                sort(by(distanceFrom(argumentClasses), Numbers.ascending())).
                headOption().
                map(Methods.<T>invokeOn(instance, args));
    }

    private Class<?> declaringClass(Method method, Object instance) {
        return isStatic(method.getModifiers()) ? method.getDeclaringClass() : instance.getClass();
    }

    private Object instance(Method method) {return isStatic(method.getModifiers()) ? null : enclosingInstance(this);}

    private static Mapper<Method, Number> distanceFrom(final Sequence<Class<?>> argumentClasses) {
        return new Mapper<Method, Number>() {
            @Override
            public Number call(Method method) throws Exception {
                return distanceFrom(argumentClasses, sequence(method.getParameterTypes()));
            }
        };
    }

    static Number distanceFrom(Sequence<Class<?>> argumentClasses, Sequence<Class<?>> parameterTypes) {
        return argumentClasses.zip(parameterTypes).map(distanceBetween().pair()).reduce(sum);
    }

    private static Function2<Class<?>, Class<?>, Number> distanceBetween() {
        return new Function2<Class<?>, Class<?>, Number>() {
            @Override
            public Number call(Class<?> argument, Class<?> parameter) throws Exception {
                return distanceBetween(argument, parameter);
            }
        };
    }

    static Number distanceBetween(Class<?> argument, Class<?> parameterType) {
        if (argument.equals(parameterType)) return 0;
        return Numbers.add(parameterType.isInterface() ? 1 : 1.1, sequence(argument.getInterfaces()).
                add(argument.getSuperclass()).
                filter(not(nullValue())).
                map(distanceBetween().flip().apply(parameterType)).
                reduce(minimum));
    }

    private static LogicalPredicate<Class<?>[]> matches(final Sequence<Class<?>> argumentClasses) {
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

    private static LogicalPredicate<Class<?>[]> exactMatch(final Sequence<Class<?>> argumentClasses) {
        return new LogicalPredicate<Class<?>[]>() {
            @Override
            public boolean matches(Class<?>[] classes) {
                return sequence(classes).equals(argumentClasses, new LogicalPredicate<Pair<Class<?>, Class<?>>>() {
                    @Override
                    public boolean matches(Pair<Class<?>, Class<?>> pair) {
                        return pair.first().equals(pair.second());
                    }
                });
            }
        };
    }
}
