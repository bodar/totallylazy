package com.googlecode.totallylazy.structural;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Methods;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Methods.allMethods;
import static com.googlecode.totallylazy.Monad.methods.sequenceO;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.reflect.Proxy.newProxyInstance;

public class Structural {
    public static <T> boolean instanceOf(final Class<?> structuralType, final Object instance) {
        return castOption(structuralType, instance).isDefined();
    }

    public static <T> T cast(final Class<T> structuralType, final Object instance) {
        return castOption(structuralType, instance).getOrThrow(new ClassCastException("Does not comply with structural contract"));
    }

    public static <T> Option<T> castOption(final Class<T> structuralType, final Object instance) {
        return extractMethods(instance, structuralType).map(new Function<Map<Method, Method>, T>() {
            @Override
            public T call(final Map<Method, Method> methods) throws Exception {
                return Unchecked.cast(newProxyInstance(structuralType.getClassLoader(), new Class[]{structuralType},
                        (o, method, objects) -> Methods.invoke(methods.get(method), instance, objects)));
            }
        });
    }

    private static <T> Option<Map<Method, Method>> extractMethods(final Object instance, Class<T> structuralType) {
        final Seq<Method> requiredMethods = sequence(structuralType.getMethods());
        final Seq<Method> instanceMethods = allMethods(instance.getClass());
        return  sequenceO(requiredMethods.map(findMethod(instanceMethods))).map(new Function<Seq<Method>, Map<Method, Method>>() {
            @Override
            public Map<Method, Method> call(Seq<Method> foundMethods) throws Exception {
                return Maps.map(requiredMethods.zip(foundMethods));
            }
        });
    }

    private static Function<Method, Option<Method>> findMethod(final Seq<Method> instanceMethods) {
        return new Function<Method, Option<Method>>() {
            @Override
            public Option<Method> call(Method requiredMethod) throws Exception {
                return findMethod(requiredMethod, instanceMethods);
            }
        };
    }

    private static Option<Method> findMethod(final Method requiredMethod, final Seq<Method> instanceMethods) {
        return instanceMethods.find(structuralMatch(requiredMethod));
    }

    private static LogicalPredicate<Method> structuralMatch(final Method required) {
        final Seq<Type> requiredParameters = sequence(required.getGenericParameterTypes());
        final Seq<Type> requiredReturnType = sequence(required.getGenericReturnType());
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
