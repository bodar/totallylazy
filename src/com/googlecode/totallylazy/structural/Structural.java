package com.googlecode.totallylazy.structural;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Methods;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.proxy.Proxy;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Methods.allMethods;
import static com.googlecode.totallylazy.Monad.methods.sequenceO;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Structural {
    public static boolean instanceOf(final Class<?> structuralType, final Object instance) {
        return castOption(structuralType, instance).isDefined();
    }

    public static <T> T cast(final Class<T> structuralType, final Object instance) {
        return castOption(structuralType, instance).getOrThrow(new ClassCastException("Does not comply with structural contract"));
    }

    public static <T> Option<T> castOption(final Class<T> structuralType, final Object instance) {
        return extractMethods(instance, structuralType).map(new Mapper<Map<Method, Method>, T>() {
            @Override
            public T call(final Map<Method, Method> methods) throws Exception {
                return Proxy.createProxy(structuralType, new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return Methods.invoke(methods.get(method), instance, objects);
                    }
                });
            }
        });
    }

    private static <T> Option<Map<Method, Method>> extractMethods(final Object instance, Class<T> structuralType) {
        final Sequence<Method> requiredMethods = sequence(structuralType.getMethods());
        final Sequence<Method> instanceMethods = allMethods(instance.getClass());
        return  sequenceO(requiredMethods.map(findMethod(instanceMethods))).map(new Mapper<Sequence<Method>, Map<Method, Method>>() {
            @Override
            public Map<Method, Method> call(Sequence<Method> foundMethods) throws Exception {
                return Maps.map(requiredMethods.zip(foundMethods));
            }
        });
    }

    private static Mapper<Method, Option<Method>> findMethod(final Sequence<Method> instanceMethods) {
        return new Mapper<Method, Option<Method>>() {
            @Override
            public Option<Method> call(Method requiredMethod) throws Exception {
                return findMethod(requiredMethod, instanceMethods);
            }
        };
    }

    private static Option<Method> findMethod(final Method requiredMethod, final Sequence<Method> instanceMethods) {
        return instanceMethods.find(structuralMatch(requiredMethod));
    }

    private static LogicalPredicate<Method> structuralMatch(final Method requiredMethod) {
        final Sequence<Type> requiredParameters = sequence(requiredMethod.getGenericParameterTypes());
        final Sequence<Type> requiredReturnType = sequence(requiredMethod.getGenericReturnType());
        return new LogicalPredicate<Method>() {
            @Override
            public boolean matches(Method objectsMethod) {
                return objectsMethod.getName().equals(requiredMethod.getName()) &&
                        sequence(objectsMethod.getGenericParameterTypes()).equals(requiredParameters) &&
                        sequence(objectsMethod.getGenericReturnType()).equals(requiredReturnType);
            }
        };
    }
}
