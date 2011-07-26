package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.records.MapRecord;
import org.w3c.dom.Node;

public class NodeRecord extends MapRecord{
    private final Node node;

    public NodeRecord(Node node) {
        this.node = node;
    }

    public Node node() {
        return node;
    }
}
