package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.xml.mappings.Mapping;
import com.googlecode.totallylazy.records.xml.mappings.Mappings;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

public class XmlSequence extends Sequence<Record> {
    private final NodeList nodes;
    private final Mappings mappings;
    private final Sequence<Keyword> definitions;

    public XmlSequence(NodeList nodes, Mappings mappings, Sequence<Keyword> definitions) {
        this.nodes = nodes;
        this.mappings = mappings;
        this.definitions = definitions;
    }

    public Iterator<Record> iterator() {
        return Iterators.map(new NodeIterator(nodes), asRecord());
    }

    private Callable1<? super Node, Record> asRecord() {
        return new Callable1<Node, Record>() {
            public Record call(final Node node) throws Exception {
                return definitions.fold(new MapRecord(), new Callable2<Record, Keyword, Record>() {
                    public Record call(Record record, Keyword keyword) throws Exception {
                        Object value = mappings.get(keyword.forClass()).from(Xml.selectNodes(node, keyword.toString()));
                        return record.set(keyword, value );
                    }
                });
            }
        };
    }
}
