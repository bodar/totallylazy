package com.googlecode.totallylazy.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class NodeArrayList<T extends Node> implements NodeList {
    private final List<T> texts;

    public NodeArrayList(Iterable<T> texts) {
        this.texts = sequence(texts).toList();
    }

    @Override
    public Node item(int index) {
        return texts.get(index);
    }

    @Override
    public int getLength() {
        return texts.size();
    }
}
