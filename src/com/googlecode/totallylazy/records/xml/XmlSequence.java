package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import java.util.Iterator;

public class XmlSequence extends Sequence<Record> {
    private final NodeList nodes;
    private final XPath xpath;

    public XmlSequence(NodeList nodes, XPath xpath) {
        this.nodes = nodes;
        this.xpath = xpath;
    }

    public Iterator<Record> iterator() {
        return new NodeIterator(nodes, xpath);
    }
}
