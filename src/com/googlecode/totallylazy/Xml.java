package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.NodeIterator;
import com.googlecode.totallylazy.iterators.PoppingIterator;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.xml.FunctionResolver.resolver;
import static java.lang.Integer.getInteger;

public class Xml {
    public static final Escaper DEFAULT_ESCAPER = new Escaper().
            withRule('&', "&amp;").
            withRule('<', "&lt;").
            withRule('>', "&gt;").
            withRule('\'', "&#39;").
            withRule('"', "&quot;").
            withRule(Strings.unicodeControlOrUndefinedCharacter(), toXmlEntity());

    public static String selectContents(final Node node, final String expression) {
        return contents(internalSelectNodes(node, expression));
    }

    public static Sequence<Node> selectNodes(final Node node, final String expression) {
        return internalSelectNodes(node, expression);
    }

    public static Sequence<Node> selectNodesForwardOnly(final Node node, final String expression) {
        return Sequences.forwardOnly(new PoppingIterator<Node>(selectNodes(node, expression).toList().iterator()));
    }

    public static Number selectNumber(final Node node, final String expression) {
        try {
            return (Number) xpathExpression(expression).evaluate(node, XPathConstants.NUMBER);
        } catch (XPathExpressionException e) {
            throw LazyException.lazyException(e);
        }
    }

    public static boolean matches(final Node node, final String expression) {
        try {
            return (Boolean) xpathExpression(expression).evaluate(node, XPathConstants.BOOLEAN);
        } catch (XPathExpressionException e) {
            throw LazyException.lazyException(e);
        }
    }

    private static Sequence<Node> internalSelectNodes(final Node node, final String expression) {
        try {
            return sequence((NodeList) xpathExpression(expression).evaluate(node, XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            try {
                String nodeAsString = (String) xpathExpression(expression).evaluate(node, XPathConstants.STRING);
                return Sequences.<Node>sequence(documentFor(node).createTextNode(nodeAsString));
            } catch (XPathExpressionException ignore) {
                throw new IllegalArgumentException(String.format("Failed to compile xpath '%s'", expression),e);
            }
        }
    }

    private static Document documentFor(Node node) {
        return node instanceof Document ? (Document) node : node.getOwnerDocument();
    }


    public static Node expectNode(final Node node, String xpath) {
        Option<Node> foundNode = selectNode(node, xpath);
        if(foundNode.isEmpty())
            throw new NoSuchElementException("No node for xpath " + xpath);
        return foundNode.get();
    }

    public static Option<Node> selectNode(final Node node, final String expression) {
        return selectNodes(node, expression).headOption();
    }

    public static Sequence<Element> selectElements(final Node node, final String expression) {
        return selectNodes(node, expression).safeCast(Element.class);
    }

    public static Element expectElement(final Node node, String xpath) {
        Option<Element> element = selectElement(node, xpath);
        if(element.isEmpty())
            throw new NoSuchElementException("No element for xpath " + xpath);
        return element.get();
    }

    public static Option<Element> selectElement(final Node node, final String expression) {
        return selectElements(node, expression).headOption();
    }

    private static ThreadLocal<XPath> xpath = new ThreadLocal<XPath>() {
        @Override
        protected XPath initialValue() {
            return internalXpath();
        }
    };

    private static XPath internalXpath() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setXPathFunctionResolver(resolver);
        return xPath;
    }

    public static XPath xpath() {
        return xpath.get();
    }

    private static ThreadLocal<Map<String, XPathExpression>> expressions = new ThreadLocal<Map<String, XPathExpression>>() {
        @Override
        protected Map<String, XPathExpression> initialValue() {
            return Maps.lruMap(getInteger("totallylazy.xpath.cache.size", 1000));
        }
    };

    private static XPathExpression xpathExpression(String expression) throws XPathExpressionException {
        Map<String, XPathExpression> expressionMap = expressions.get();
        if (!expressionMap.containsKey(expression)) {
            expressionMap.put(expression, xpath().compile(expression));
        }
        return expressionMap.get(expression);
    }

    public static Sequence<Node> sequence(final NodeList nodes) {
        return new Sequence<Node>() {
            public Iterator<Node> iterator() {
                return new NodeIterator(nodes);
            }
        };
    }

    public static String contents(Sequence<Node> nodes) {
        return contentsSequence(nodes).toString("");
    }

    public static Sequence<String> contentsSequence(Sequence<Node> nodes) {
        return nodes.map(contents());
    }

    public static Sequence<String> textContents(Sequence<Node> nodes) {
        return nodes.map(textContent());
    }

    public static Sequence<String> textContents(NodeList nodes) {
        return Xml.textContents(Xml.sequence(nodes));
    }

    public static final Function1<Node, String> textContent =  new Function1<Node, String>() {
        @Override
        public String call(Node node) throws Exception {
            return node.getTextContent();
        }
    };

    public static Function1<Node, String> textContent() {
        return textContent;
    }

    public static Block<Element> removeAttribute(final String name) {
        return new Block<Element>() {
            @Override
            protected void execute(Element element) throws Exception {
                element.removeAttribute(name);
            }
        };
    }

    public static Function1<Node, String> contents() {
        return new Function1<Node, String>() {
            public String call(Node node) throws Exception {
                return contents(node);
            }
        };
    }

    public static String contents(Node node) throws Exception {
        if (node instanceof Attr) {
            return contents((Attr) node);
        }
        if (node instanceof CharacterData) {
            return contents((CharacterData) node);
        }
        if (node instanceof Element) {
            return contents((Element) node);
        }
        throw new UnsupportedOperationException("Unknown node type " + node.getClass());
    }

    public static String contents(CharacterData characterData) {
        return characterData.getData();
    }

    public static String contents(Attr attr) {
        return attr.getValue();
    }

    public static String contents(Element element) throws Exception {
        return sequence(element.getChildNodes()).map(new Callable1<Node, String>() {
            public String call(Node node) throws Exception {
                if (node instanceof Element) {
                    return asString((Element) node);
                }
                return contents(node);
            }
        }).toString("");

    }

    public static String asString(Node node) throws Exception {
        Transformer transformer = transformer();
        StringWriter writer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public static Transformer transformer() throws TransformerConfigurationException {
        return internalTransformer();
    }

    public static Transformer transformer(Pair<String, Object>... attributes) throws TransformerConfigurationException {
        return internalTransformer(attributes);
    }

    private static Transformer internalTransformer(Pair<String, Object>... attributes) throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        for (Pair<String, Object> attribute : attributes) {
            transformerFactory.setAttribute(attribute.first(), attribute.second());
        }
        return transformerFactory.newTransformer();
    }

    public static Document document(byte[] bytes) {
        return document(string(bytes));
    }

    public static Document document(String xml) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver(ignoreEntities());
            documentBuilder.setErrorHandler(null);
            return documentBuilder.parse(new ByteArrayInputStream(bytes(xml)));
        } catch (Exception e) {
            throw LazyException.lazyException(e);
        }
    }

    private static EntityResolver ignoreEntities() {
        return new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        };
    }

    public static Sequence<Node> remove(final Node root, final String expression) {
        return remove(selectNodes(root, expression));
    }

    public static Sequence<Node> remove(final Sequence<Node> nodes) {
        return nodes.map(remove()).realise();
    }

    private static Function1<Node, Node> remove() {
        return new Function1<Node, Node>() {
            public Node call(Node node) throws Exception {
                return node.getParentNode().removeChild(node);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static String format(final Node node) throws Exception {
        return format(node, new Pair[0]);
    }

    public static String format(final Node node, final Pair<String, Object>... attributes) throws Exception {
        Transformer transformer = transformer(attributes);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    public static String escape(Object value) {
        return DEFAULT_ESCAPER.
                escape(value);
    }

    public static Function1<Object, String> escape() {
        return new Function1<Object, String>() {
            public String call(Object value) throws Exception {
                return escape(value);
            }
        };
    }

    public static Function1<Character, String> toXmlEntity() {
        return new Function1<Character, String>() {
            public String call(Character character) throws Exception {
                return String.format("&#%s;", Integer.toString(character, 10));
            }
        };
    }

    public static class functions {
        public static Function1<Element, Element> setAttribute(final String name, final String value) {
            return new Function1<Element, Element>() {
                public Element call(Element element) throws Exception {
                    element.setAttribute(name,value);
                    return element;
                }
            };
        }

        public static Predicate<Node> matches(final String expression) {
            return new Predicate<Node>() {
                @Override
                public boolean matches(Node node) {
                    return Xml.matches(node, expression);
                }
            };
        }

        public static Function1<Element, String> attribute(final String attributeName) {
            return new Function1<Element, String>() {
                public String call(Element element) throws Exception {
                    return element.getAttribute(attributeName);
                }
            };
        }


        public static Function2<Node, String, String> selectContents() {
            return new Function2<Node, String, String>() {
                @Override
                public String call(Node node, String expression) throws Exception {
                    return Xml.selectContents(node, expression);
                }
            };
        }

        public static Function1<Node, String> selectContents(final String expression) {
            return new Function1<Node, String>() {
                @Override
                public String call(Node node) throws Exception {
                    return Xml.selectContents(node, expression);
                }
            };
        }

        public static Function2<Node, String, Sequence<Node>> selectNodes() {
            return new Function2<Node, String, Sequence<Node>>() {
                @Override
                public Sequence<Node> call(final Node node, final String expression) throws Exception {
                    return Xml.selectNodes(node, expression);
                }
            };
        }

        public static Function1<String, Document> document() {
            return new Function1<String, Document>() {
                @Override
                public Document call(String value) throws Exception {
                    return Xml.document(value);
                }
            };
        }
    }
}