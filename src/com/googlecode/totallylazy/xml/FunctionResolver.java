package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Methods;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Methods.methodName;
import static com.googlecode.totallylazy.Methods.modifier;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Collections.newSetFromMap;

public enum FunctionResolver implements XPathFunctionResolver {
    resolver;

    private static final Set<Method> methods = newSetFromMap(new ConcurrentHashMap<Method, Boolean>());

    @Override
    public XPathFunction resolveFunction(final QName functionName, int arity) {
        return new XPathFunction() {
            @Override
            public Object evaluate(List args) throws XPathFunctionException {
                String name = functionName.getLocalPart().replace("-", "");
                Method method = sequence(methods).find(where(methodName(), equalIgnoringCase(name))).get();
                try {
                    return Methods.invoke(method, null, args.toArray());
                } catch (Exception e) {
                    return Methods.invoke(method, null, args);
                }
            }
        };
    }

    public static void add(Class<?> aClassWithStaticMethods) {
        methods.addAll(sequence(aClassWithStaticMethods.getMethods()).
                filter(and(modifier(Modifier.PUBLIC), modifier(STATIC))).toList());
    }
}
