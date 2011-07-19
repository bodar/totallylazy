package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;

public class NodeIterator extends ReadOnlyIterator<Record> {
    private final NodeList nodes;
    private final XPath xpath;
    private int index;

    public NodeIterator(NodeList nodes, XPath xpath) {
        this.nodes = nodes;
        this.xpath = xpath;
    }

    public boolean hasNext() {
        return index < nodes.getLength();
    }

    public Record next() {
        return new NodeRecord(nodes.item(index++), xpath);
    }
}
