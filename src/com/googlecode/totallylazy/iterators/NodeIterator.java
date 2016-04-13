package com.googlecode.totallylazy.iterators;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.NoSuchElementException;

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
        if(hasNext())
            return nodes.item(index++);
        throw new NoSuchElementException();
    }
}
