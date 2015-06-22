package com.googlecode.totallylazy.structural;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.reflection.Methods;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.reflection.Methods.allMethods;
import static com.googlecode.totallylazy.Monad.methods.sequenceO;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.reflect.Proxy.newProxyInstance;

public class Structural {
    public static boolean instanceOf(final Class<?> structuralType, final Object instance) {
        return castOption(structuralType, instance).isDefined();
    }

    public static <T> T cast(final Class<T> structuralType, final Object instance) {
        return castOption(structuralType, instance).getOrThrow(new ClassCastException("Does not comply with structural contract"));
    }

    public static <T> Option<T> castOption(final Class<T> structuralType, final Object instance) {
        return extractMethods(instance, structuralType).
                map(methods -> Unchecked.cast(newProxyInstance(structuralType.getClassLoader(), new Class[]{structuralType},
                (o, method, objects) -> Methods.invoke(methods.get(method), instance, objects))));
    }

    private static <T> Option<Map<Method, Method>> extractMethods(final Object instance, Class<T> structuralType) {
        final Sequence<Method> requiredMethods = sequence(structuralType.getMethods());
        final Sequence<Method> instanceMethods = allMethods(instance.getClass());
        return  sequenceO(requiredMethods.map(findMethod(instanceMethods))).map(foundMethods -> map(requiredMethods.zip(foundMethods)));
    }

    private static Function1<Method, Option<Method>> findMethod(final Sequence<Method> instanceMethods) {
        return requiredMethod -> findMethod(requiredMethod, instanceMethods);
    }

    private static Option<Method> findMethod(final Method requiredMethod, final Sequence<Method> instanceMethods) {
        return instanceMethods.find(structuralMatch(requiredMethod));
    }

    private static LogicalPredicate<Method> structuralMatch(final Method required) {
        final Sequence<Type> requiredParameters = sequence(required.getGenericParameterTypes());
        final Sequence<Type> requiredReturnType = sequence(required.getGenericReturnType());
        return new LogicalPredicate<Method>() {
            @Override
            public boolean matches(Method objects) {
                return objects.getName().equals(required.getName()) &&
                        sequence(objects.getGenericParameterTypes()).equals(requiredParameters) &&
                        sequence(objects.getGenericReturnType()).equals(requiredReturnType);
            }
        };
    }
}
