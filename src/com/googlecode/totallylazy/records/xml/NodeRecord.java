package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.Node;

public class NodeRecord implements Record {
    private final Node node;

    public NodeRecord(Node node) {
        this.node = node;
    }

    public <T> T get(Keyword<T> keyword) {
        return (T) Xml.selectContents(node, keyword.toString());
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
