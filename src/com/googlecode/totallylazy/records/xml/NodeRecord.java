package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class NodeRecord implements Record {
    private final Node node;
    private final XPath xpath;

    public NodeRecord(Node node, XPath xpath) {
        this.node = node;
        this.xpath = xpath;
    }

    public <T> T get(Keyword<T> keyword) {
        try {
            return (T) xpath.evaluate(keyword.toString(), node, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new LazyException(e);
        }
    }

    private String toString(Node node) {
        Document document = node.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        return serializer.writeToString(node);
    }

    public <T> Record set(Keyword<T> name, T value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<Pair<Keyword, Object>> fields() {
        throw new UnsupportedOperationException();
    }

    public Sequence<Keyword> keywords() {
        throw new UnsupportedOperationException();
    }

    public Sequence<Object> getValuesFor(Sequence<Keyword> keywords) {
        throw new UnsupportedOperationException();
    }
}
