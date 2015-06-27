package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.reflection.Methods;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.reflection.Methods.annotation;
import static com.googlecode.totallylazy.reflection.Methods.modifier;
import static com.googlecode.totallylazy.predicates.Predicates.and;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.notNullValue;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static com.googlecode.totallylazy.xml.XPathFunction.functions.value;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Collections.newSetFromMap;

public enum FunctionResolver implements XPathFunctionResolver {
    resolver(XPathFunctions.class, XPathLookups.class);

    private final Set<Method> methods = newSetFromMap(new ConcurrentHashMap<Method, Boolean>());

    FunctionResolver(Class<?>... defaultXpathFunctions) {
        for (Class<?> functions : defaultXpathFunctions) {
            add(functions);
        }
    }

    @Override
    public XPathFunction resolveFunction(final QName functionName, int arity) {
        return args -> {
            String name = functionName.getLocalPart();
            Method method = sequence(methods).
                    find(where(annotation(com.googlecode.totallylazy.xml.XPathFunction.class).then(value()), is(name))).get();
            try {
                return Methods.invoke(method, null, args.toArray());
            } catch (Exception e) {
                return Methods.invoke(method, null, args);
            }
        };
    }

    public void add(Class<?> aClassWithStaticMethods) {
        methods.addAll(sequence(aClassWithStaticMethods.getMethods()).
                filter(and(modifier(Modifier.PUBLIC), modifier(STATIC),
                        where(annotation(com.googlecode.totallylazy.xml.XPathFunction.class), is(notNullValue())))).toList());
    }
}
