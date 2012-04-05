package com.googlecode.totallylazy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XPathFunctions {
    public static final Map<String, Callable1<List, Object>> functions = new ConcurrentHashMap<String, Callable1<List, Object>>() {
        {
            put("join-strings", joinStrings());
        }

        private Callable1<List, Object> joinStrings() {
            return new Callable1<List, Object>() {
                @Override
                public Object call(List list) throws Exception {
                    return Xml.sequence((NodeList) list.get(0)).map(toNodeValues()).toString((String) list.get(1));
                }

                private Function1<Node, String> toNodeValues() {
                    return new Function1<Node, String>() {
                        @Override
                        public String call(Node node) throws Exception {
                            return node.getTextContent();
                        }
                    };
                }
            };
        }
    };

    static class FunctionAdapter implements XPathFunction {
        private final Callable1<List, Object> callable1;

        public FunctionAdapter(Callable1<List, Object> callable1) {
            this.callable1 = callable1;
        }

        @Override
        public Object evaluate(List args) throws XPathFunctionException {
            try {
                return callable1.call(args);
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    static class TotallyLazyXPathFunction implements XPathFunctionResolver {
        private TotallyLazyXPathFunction() { }

        static TotallyLazyXPathFunction totallyLazyXPathFunctions() {
            return new TotallyLazyXPathFunction();
        }

        @Override
        public XPathFunction resolveFunction(QName functionName, int arity) {
            return new FunctionAdapter(functions.get(functionName.getLocalPart()));
        }
    }
}