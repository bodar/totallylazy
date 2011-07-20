package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import static com.googlecode.totallylazy.records.xml.Xml.asString;

public class NodeRecord implements Record {
    private final Node node;
    private final XPath xpath;

    public NodeRecord(Node node) {
        this.node = node;
        this.xpath = Xml.xpath();
    }

    public <T> T get(Keyword<T> keyword) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate(keyword.toString(), node, XPathConstants.NODESET);
            return (T) asString(nodes);
        } catch (XPathExpressionException e) {
            throw new LazyException(e);
        }
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

    public static Callable1<Node, Record> asRecord() {
        return new Callable1<Node, Record>() {
            public Record call(Node node) throws Exception {
                return new NodeRecord(node);
            }
        };
    }

}
