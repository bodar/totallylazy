package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SourceRecord;
import com.googlecode.totallylazy.records.xml.mappings.Mapping;
import com.googlecode.totallylazy.records.xml.mappings.Mappings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;

import static com.googlecode.totallylazy.Xml.xpath;

public class XmlRecords extends AbstractRecords {
    private final XPath xpath = xpath();
    private final Document document;
    private final Mappings mappings;

    public XmlRecords(Document document, Mappings mappings) {
        this.document = document;
        this.mappings = mappings;
    }

    public XmlRecords(Document document) {
        this(document, new Mappings());
    }

    public Sequence<Record> get(Keyword recordName) {
        Sequence<Node> nodes = Xml.selectNodes(document, recordName.toString());
        return new XmlSequence(nodes, mappings, definitions(recordName));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        for (Record record : records) {
            Element newElement = record.keywords().fold(document.createElement(toTagName(recordName.toString())), addNodes(record));
            Node parent = Xml.selectNodes(document, toParent(recordName)).head();
            parent.appendChild(newElement);
        }
        return records.size();
    }

    private String toParent(Keyword recordName) {
        String xpath = recordName.toString();
        String[] parts = xpath.split("/");
        return Sequences.sequence(parts).take(parts.length - 1).toString("/");
    }

    private Callable2<? super Element, ? super Keyword, Element> addNodes(final Record record) {
        return new Callable2<Element, Keyword, Element>() {
            public Element call(Element container, Keyword field) throws Exception {
                Object value = record.get(field);
                if (value != null) {
                    Mapping<Object> objectMapping = mappings.get(field.forClass());
                    Sequence<Node> nodes = objectMapping.to(document, field.toString(), value);
                    for (Node node : nodes) {
                        container.appendChild(node);
                    }
                }
                return container;
            }
        };
    }

    public static String toTagName(final String expression) {
        String[] parts = expression.split("/");
        return parts[parts.length - 1];
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        Sequence<Node> map = get(recordName).filter(predicate).map(asNode());
        return Xml.remove(map).size();
    }

    private Callable1<? super Record, Node> asNode() {
        return new Callable1<Record, Node>() {
            public Node call(Record record) throws Exception {
                return ((SourceRecord<Node>) record).value();
            }
        };
    }

    public Number remove(Keyword recordName) {
        return Xml.remove(document, recordName.toString()).size();
    }
}
