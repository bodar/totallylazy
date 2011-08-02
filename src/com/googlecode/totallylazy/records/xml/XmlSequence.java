package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.xml.mappings.Mappings;
import org.w3c.dom.Node;

import java.util.Iterator;

public class XmlSequence extends Sequence<Record> {
    private final Sequence<Node> nodes;
    private final Mappings mappings;
    private final Sequence<Keyword> definitions;

    public XmlSequence(Sequence<Node> nodes, Mappings mappings, Sequence<Keyword> definitions) {
        this.nodes = nodes;
        this.mappings = mappings;
        this.definitions = definitions;
    }

    public Iterator<Record> iterator() {
        return nodes.map(asRecord()).iterator();
    }

    private Callable1<? super Node, Record> asRecord() {
        return new Callable1<Node, Record>() {
            public Record call(final Node node) throws Exception {
                return definitions.fold(new NodeRecord(node), new Callable2<Record, Keyword, Record>() {
                    public Record call(Record nodeRecord, Keyword keyword) throws Exception {
                        Sequence<Node> nodes = Xml.selectNodes(node, keyword.name());
                        if (nodes.isEmpty()) {
                            return nodeRecord;
                        }
                        Object value = mappings.get(keyword.forClass()).from(nodes);
                        return nodeRecord.set(keyword, value);
                    }
                });
            }
        };
    }
}
