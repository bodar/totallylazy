package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.instanceOf;

public class XPathFunctions {
    private static final Rules<Pair<String, List<Object>>, Object> functions = Rules.rules();

    static {
        add(signature("string-join", Predicates.<List<Object>>and(argInstanceOf(0, NodeList.class), argInstanceOf(1, String.class))), nodeListAndString(joinStrings()));
        add(signature("trim-and-join", Predicates.<List<Object>>and(argInstanceOf(0, NodeList.class), argInstanceOf(1, String.class))), nodeListAndString(trimAndJoin()));
    }

    public static Rules<Pair<String, List<Object>>, Object> add(Predicate<Pair<String, List<Object>>> signature, Function1<Second<List<Object>>, Object> callable) {
        return functions.add(signature, callable);
    }

    public static Predicate<Pair<String, List<Object>>> signature(final String methodName, final Predicate<List<Object>> argsPredicate) {
        return new Predicate<Pair<String, List<Object>>>() {
            @Override
            public boolean matches(Pair<String, List<Object>> other) {
                return other.first().equals(methodName) && argsPredicate.matches(other.second());
            }
        };
    }

    private static String joinStrings(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).toString(delimiter);
    }

    private static String trimAndJoin(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).map(Strings.trim()).toString(delimiter);
    }

    private static String unescape(String value) {
        return value.replace("\\n", "\n");
    }

    public static XPathFunctionResolver resolver() {
        return new XPathFunctionResolver() {
            @Override
            public XPathFunction resolveFunction(final QName functionName, int arity) {
                return new XPathFunction() {
                    @Override
                    public Object evaluate(List args) throws XPathFunctionException {
                        try {
                            Pair<String, List<Object>> pair = Pair.pair(functionName.getLocalPart(), Unchecked.<List<Object>>cast(args));
                            return functions.call(pair);
                        } catch (Exception e) {
                            throw new XPathFunctionException(e);
                        }
                    }
                };
            }
        };
    }

    private static Callable2<NodeList, String, String> trimAndJoin() {
        return new Callable2<NodeList, String, String>() {
            @Override
            public String call(NodeList nodes, String delimiter) throws Exception {
                return trimAndJoin(nodes, delimiter);
            }
        };
    }

    private static Callable2<NodeList, String, String> joinStrings() {
        return new Callable2<NodeList, String, String>() {
            @Override
            public String call(NodeList nodes, String delimiter) throws Exception {
                return joinStrings(nodes, delimiter);
            }
        };
    }

    private static Function1<Second<List<Object>>, Object> nodeListAndString(final Callable2<NodeList, String, String> callable) {
        return Callables.<List<Object>>second().then(new Callable1<List<Object>, Object>() {
            @Override
            public Object call(List<Object> objects) throws Exception {
                return callable.call((NodeList) objects.get(0), unescape((String) objects.get(1)));
            }
        });
    }

    private static LogicalPredicate<List<Object>> argInstanceOf(int index, Class<?> clazz) {
        return Predicates.where(arg(index), instanceOf(clazz));
    }

    private static Function1<List<Object>, Object> arg(final int index) {
        return new Function1<List<Object>, Object>() {
            @Override
            public Object call(List<Object> objects) throws Exception {
                return objects.get(index);
            }
        };
    }
}