package com.googlecode.totallylazy;

import com.googlecode.totallylazy.regex.Regex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Sequences.sequence;

public class XPathFunctions {
    private static final Rules<Pair<String, List<Object>>, Object> functions = Rules.rules();

    static {
        add(signature("string-join", argumentsOf(NodeList.class, String.class)), nodeListAndString(joinStrings()));
        add(signature("trim-and-join", argumentsOf(NodeList.class, String.class)), nodeListAndString(trimAndJoin()));
        add(signature("if", argumentsOf(NodeList.class, Object.class, Object.class)), threeNodeLists(ifElse()));
        add(signature("or", Predicates.<List<Object>>all()), returnFirstNonNullOrEmpty());
        add(signature("tokenize", argumentsOf(NodeList.class, String.class)), stringStringAndNodeList(tokenize()));
    }

    private static Function2<NodeList, String, NodeList> tokenize() {
        return new Function2<NodeList, String, NodeList>() {
            @Override
            public NodeList call(final NodeList input, final String pattern) throws Exception {
                return new NodeArrayList<Text>(Xml.sequence(input).flatMap(split(pattern)));

            }
        };
    }

    private static Function1<Node, Sequence<Text>> split(final String pattern) {
        return new Function1<Node, Sequence<Text>>() {
            @Override
            public Sequence<Text> call(final Node node) throws Exception {
                return Regex.regex(pattern).split(node.getTextContent()).map(createText(node));                    }
        };
    }

    private static Function1<String, Text> createText(final Node node) {
        return new Function1<String, Text>() {
            @Override
            public Text call(String text) throws Exception {
                return node.getOwnerDocument().createTextNode(text);
            }
        };
    }

    private static Function1<List<Object>, Object> returnFirstNonNullOrEmpty() {
        return new Function1<List<Object>, Object>() {
            @Override
            public Object call(List<Object> arguments) throws Exception {
                return sequence(arguments).find(not(nullValue()).and(new Predicate<Object>() {
                    @Override
                    public boolean matches(Object other) {
                        return !(other instanceof NodeList) || ((NodeList) other).getLength() != 0;
                    }
                })).getOrNull();
            }
        };
    }

    private static Function3<NodeList, Object, Object, Object> ifElse() {
        return new Function3<NodeList, Object, Object, Object>() {
            @Override
            public Object call(NodeList nodeList, Object matched, Object notMatched) throws Exception {
                return nodeList.getLength() > 0 ? matched : notMatched;
            }
        };
    }

    public static Rules<Pair<String, List<Object>>, Object> add(Predicate<Pair<String, List<Object>>> signature, final Function1<List<Object>, ?> callable) {
        return functions.addLast(signature, new Function1<Pair<String, List<Object>>, Object>() {
            @Override
            public Object call(Pair<String, List<Object>> pair) throws Exception {
                return callable.call(pair.second());
            }
        });
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

    private static Function1<List<Object>, String> nodeListAndString(final Callable2<NodeList, String, String> callable) {
        return new Function1<List<Object>, String>() {
            @Override
            public String call(List<Object> objects) throws Exception {
                return callable.call((NodeList) objects.get(0), unescape((String) objects.get(1)));
            }
        };
    }

    private static Function1<List<Object>, NodeList> stringStringAndNodeList(final Function2<NodeList, String, NodeList> function) {
        return new Function1<List<Object>, NodeList>() {
            @Override
            public NodeList call(List<Object> objects) throws Exception {
                return function.call((NodeList) objects.get(0), (String) objects.get(1));
            }
        };
    }

    private static Function1<List<Object>, Object> threeNodeLists(final Callable3<NodeList, Object, Object, Object> callable) {
        return new Function1<List<Object>, Object>() {
            @Override
            public Object call(List<Object> objects) throws Exception {
                return callable.call((NodeList) objects.get(0), objects.get(1), objects.get(2));
            }
        };
    }

    private static Function1<List<Object>, Object> arg(final Number index) {
        return new Function1<List<Object>, Object>() {
            @Override
            public Object call(List<Object> objects) throws Exception {
                return objects.get(index.intValue());
            }
        };
    }

    private static Predicate<List<Object>> argumentsOf(final Class<?>... clazz) {
        return new Predicate<List<Object>>() {
            @Override
            public boolean matches(final List<Object> other) {
                return Sequences.sequence(clazz).zipWithIndex().forAll(new Predicate<Pair<Number, Class<?>>>() {
                    @Override
                    public boolean matches(Pair<Number, Class<?>> pair) {
                        return Predicates.where(arg(pair.first()), instanceOf(pair.second())).matches(other);
                    }
                });
            }
        };
    }

    public static class NodeArrayList<T extends Node> implements NodeList {
        private final List<T> texts;

        public NodeArrayList(Iterable<T> texts) {
            this.texts = sequence(texts).toList();
        }

        @Override
        public Node item(int index) {
            return texts.get(index);
        }

        @Override
        public int getLength() {
            return texts.size();
        }
    }
}