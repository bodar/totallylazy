package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Methods;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Methods.methodName;
import static com.googlecode.totallylazy.Methods.parameterTypes;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.minimum;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static java.lang.reflect.Modifier.isStatic;

public abstract class multi {
    public <T> T method(Object... args) {
        Method method = getClass().getEnclosingMethod();
        return matchMethod(isStatic(method.getModifiers()) ? null : enclosingInstance(this), method, args);
    }

    private static <T> T matchMethod(Object instance, Method method, Object[] args) {
        Sequence<Class<?>> argumentClasses = sequence(args).map(toClass());
        Method matched = sequence(method.getDeclaringClass().getDeclaredMethods()).
                remove(method).
                filter(where(methodName(), is(method.getName())).
                        and(where(parameterTypes(), matches(argumentClasses)))).
                sortBy(distanceFrom(argumentClasses)).
                head();

        return Methods.invoke(matched, instance, args);
    }

    private static Mapper<Method, Integer> distanceFrom(final Sequence<Class<?>> argumentClasses) {
        return new Mapper<Method, Integer>() {
            @Override
            public Integer call(Method method) throws Exception {
                return distanceFrom(argumentClasses, sequence(method.getParameterTypes())).intValue();
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
        return increment(sequence(argument.getInterfaces()).
                cons(argument.getSuperclass()).
                filter(not(nullValue())).
                map(distanceBetween().flip().apply(parameterType)).
                reduce(minimum));
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
