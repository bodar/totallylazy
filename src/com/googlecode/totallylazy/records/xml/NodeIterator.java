package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeIterator extends ReadOnlyIterator<Node> {
    private final NodeList nodes;
    private int index;

    public NodeIterator(NodeList nodes) {
        this.nodes = nodes;
    }

    public boolean hasNext() {
        return index < nodes.getLength();
    }

    public Node next() {
        return nodes.item(index++);
    }
}
