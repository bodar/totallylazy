package com.googlecode.totallylazy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;

import static com.googlecode.totallylazy.Unchecked.cast;

public class XPathFunctions {
    public static final Rules<Pair<String, List<Object>>, Object> functions = Rules.rules();

    static {
        functions.add(new Predicate<Pair<String, List<Object>>>() {
            @Override
            public boolean matches(Pair<String, List<Object>> other) {
                return other.first().equals("string-join") &&
                        (other.second().get(0) instanceof NodeList) &&
                        (other.second().get(1) instanceof String);
            }
        }, joinStrings());
        functions.add(new Predicate<Pair<String, List<Object>>>() {
            @Override
            public boolean matches(Pair<String, List<Object>> other) {
                return other.first().equals("trim-and-join") &&
                        (other.second().get(0) instanceof NodeList) &&
                        (other.second().get(1) instanceof String);
            }
        }, trimAndJoin());
    }

    private static Callable1<Pair<String, List<Object>>, String> joinStrings() {
        return new Callable1<Pair<String, List<Object>>, String>() {
            @Override
            public String call(Pair<String, List<Object>> pair) throws Exception {
                Sequence<Node> nodes = Xml.sequence((NodeList) pair.second().get(0));
                String delimiter = unescape((String) pair.second().get(1));
                return Xml.textContents(nodes).toString(delimiter);
            }
        };
    }

    private static Callable1<Pair<String, List<Object>>, String> trimAndJoin() {
        return new Callable1<Pair<String, List<Object>>, String>() {
            @Override
            public String call(Pair<String, List<Object>> pair) throws Exception {
                Sequence<Node> nodes = Xml.sequence((NodeList) pair.second().get(0));
                String delimiter = unescape((String) pair.second().get(1));
                return Xml.textContents(nodes).map(Strings.trim()).toString(delimiter);
            }
        };
    }

    public static Callable1<List, Object> adapt(final Callable2<Sequence<Node>, String, String> callable) {
        return new Callable1<List, Object>() {
            @Override
            public Object call(List list) throws Exception {
                return callable.call(Xml.sequence((NodeList) list.get(0)), unescape((String) list.get(1)));
            }

        };
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
}