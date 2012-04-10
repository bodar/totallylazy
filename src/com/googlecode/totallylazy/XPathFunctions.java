package com.googlecode.totallylazy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class XPathFunctions {
    public static final Map<String, Callable1<List, Object>> functions = new ConcurrentHashMap<String, Callable1<List, Object>>() {{
        put("string-join", adapt(joinStrings()));
    }};

    public static Callable2<Sequence<Node>, String, String> joinStrings() {
        return new Callable2<Sequence<Node>, String, String>() {
            @Override
            public String call(Sequence<Node> nodes, String joinWith) throws Exception {
                return Xml.textContents(nodes).toString(joinWith);
            }
        };
    }

    public static Callable1<List, Object> adapt(final Callable2<Sequence<Node>, String, String> callable) {
        return new Callable1<List, Object>() {
            @Override
            public Object call(List list) throws Exception {
                return callable.call(Xml.sequence((NodeList) list.get(0)), unescape((String) list.get(1)));
            }

            private String unescape(String value) {
                return value.replace("\\n", "\n");
            }
        };
    }

    public static XPathFunctionResolver resolver() {
        return new XPathFunctionResolver() {
            @Override
            public XPathFunction resolveFunction(final QName functionName, int arity) {
                return new XPathFunction() {
                    @Override
                    public Object evaluate(List args) throws XPathFunctionException {
                        try {
                            return functions.get(functionName.getLocalPart()).call(args);
                        } catch (Exception e) {
                            throw new XPathFunctionException(e);
                        }
                    }
                };
            }
        };
    }
}