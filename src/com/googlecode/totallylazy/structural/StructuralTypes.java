package com.googlecode.totallylazy.structural;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Methods;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.proxy.Proxy;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Methods.allMethods;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class StructuralTypes {
    public static <T> T structrualType(Class<T> structuralType, final Object instance) {
        final Map<Method, Method> methods = extractMethods(instance, structuralType);

        return Proxy.createProxy(structuralType, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return Methods.invoke(methods.get(method), instance, objects);
            }
        });
    }

    private static <T> Map<Method, Method> extractMethods(final Object instance, Class<T> structuralType) {
        final Sequence<Method> instanceMethods = allMethods(instance.getClass());
        return map(sequence(structuralType.getMethods()).map(new Mapper<Method, Pair<Method, Method>>() {
            @Override
            public Pair<Method, Method> call(Method requiredMethod) throws Exception {
                return pair(requiredMethod, findMethod(requiredMethod, instanceMethods).getOrThrow(new ClassCastException("Does not have method: " + requiredMethod)));
            }
        }));
    }

    private static Option<Method> findMethod(final Method requiredMethod, final Sequence<Method> instanceMethods) {
        final Sequence<Type> requiredParameters = sequence(requiredMethod.getGenericParameterTypes());
        final Sequence<Type> requiredReturnType = sequence(requiredMethod.getGenericReturnType());
        return instanceMethods.find(new LogicalPredicate<Method>() {
            @Override
            public boolean matches(Method objectsMethod) {
                return objectsMethod.getName().equals(requiredMethod.getName()) &&
                        sequence(objectsMethod.getGenericParameterTypes()).equals(requiredParameters) &&
                        sequence(objectsMethod.getGenericReturnType()).equals(requiredReturnType);
            }
        });
    }
}
