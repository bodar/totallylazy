package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Iterator;

public class Xml {
    public static String selectContents(final Node node, final String expression)  {
        return contents(selectNodes(node, expression));
    }

    public static Sequence<Node> selectNodes(Node node, String expression)  {
        try {
            return sequence((NodeList) xpath().evaluate(expression, node, XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            throw new LazyException(e);
        }
    }

    public static XPath xpath() {
        return XPathFactory.newInstance().newXPath();
    }

    public static Sequence<Node> sequence(final NodeList nodes) {
        return new Sequence<Node>() {
            public Iterator<Node> iterator() {
                return new NodeIterator(nodes);
            }
        };
    }

    public static String contents(Sequence<Node> nodes) {
        return nodes.map(contents()).toString("");
    }

    public static Callable1<? super Node, String> contents() {
        return new Callable1<Node, String>() {
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

    public static String asString(Element element) throws Exception {
        Transformer transformer = transformer();
        StringWriter writer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(element), new StreamResult(writer));
        return writer.toString();
    }

    public static Transformer transformer() throws TransformerConfigurationException {
        return TransformerFactory.newInstance().newTransformer();
    }


    public static Document load(String xml) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            throw new LazyException(e);
        }
    }

    public static Sequence<Node> remove(final Node root, final String expression) {
        return remove(selectNodes(root, expression));

    }

    public static Sequence<Node> remove(final Sequence<Node> nodes) {
        return nodes.map(remove()).realise();
    }

    private static Callable1<Node, Node> remove() {
        return new Callable1<Node, Node>() {
            public Node call(Node node) throws Exception {
                return node.getParentNode().removeChild(node);
            }
        };
    }
}
