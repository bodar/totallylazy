package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.ForwardOnlySequence;
import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;

import static com.googlecode.totallylazy.Sequences.forwardOnly;

public class Xml {
    public static ForwardOnlySequence<Node> sequence(NodeList nodes) {
        return forwardOnly(new NodeIterator(nodes));
    }

    public static String asString(NodeList nodes) {
        return asString(sequence(nodes));
    }

    public static String asString(Sequence<Node> nodes) {
        return nodes.map(asString()).toString("");
    }

    public static Callable1<? super Node, String> asString() {
        return new Callable1<Node, String>() {
            public String call(Node node) throws Exception {
                return asString(node);
            }
        };
    }

    public static String asString(Node node) throws Exception {
        if (node instanceof Attr) {
            return asString((Attr) node);
        }
        if (node instanceof CharacterData) {
            return asString((CharacterData) node);
        }
        if (node instanceof Element) {
            return contentsAsString((Element) node);
        }
        throw new UnsupportedOperationException("Unknown node type " + node.getClass());
    }

    public static String asString(CharacterData characterData) {
        return characterData.getData();
    }

    public static String asString(Attr attr) {
        return attr.getValue();
    }

    public static String contentsAsString(Element element) throws Exception {
        Sequence<Node> nodes = sequence(element.getChildNodes());
        return nodes.map(new Callable1<Node, String>() {
            public String call(Node node) throws Exception {
                if(node instanceof Element){
                    return asString((Element) node);
                }
                return asString(node);
            }
        }).toString("");

    }

    public static String asString(Element element) throws TransformerException {
        Transformer transformer = transformer();
        StringWriter writer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(element), new StreamResult(writer));
        return writer.toString();
    }

    public static Transformer transformer() throws TransformerConfigurationException {
        return TransformerFactory.newInstance().newTransformer();
    }

    public static XPath xpath() {
        return XPathFactory.newInstance().newXPath();
    }
}
