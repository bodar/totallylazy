package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.NodeList;

import java.util.Iterator;

import static com.googlecode.totallylazy.records.xml.NodeRecord.asRecord;

public class XmlSequence extends Sequence<Record> {
    private final NodeList nodes;

    public XmlSequence(NodeList nodes) {
        this.nodes = nodes;
    }

    public Iterator<Record> iterator() {
        return Iterators.map(new NodeIterator(nodes), asRecord());
    }
}
