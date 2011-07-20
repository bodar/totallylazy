package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import static com.googlecode.totallylazy.records.xml.Xml.load;
import static com.googlecode.totallylazy.records.xml.Xml.xpath;

public class XmlRecords extends AbstractRecords {
    private final XPath xpath = xpath();
    private final Document document;

    public XmlRecords(String xml) {
        document = load(xml);
    }

    public XmlRecords(Document document) {
        this.document = document;
    }

    public Sequence<Record> get(Keyword recordName) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate(recordName.toString(), document, XPathConstants.NODESET);
            return new XmlSequence(nodes);
        } catch (XPathExpressionException e) {
            throw new LazyException(e);
        }
    }

    public boolean exists(Keyword recordName) {
        throw new UnsupportedOperationException();
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        for (Record record : records) {
            Element newElement = fields.fold(document.createElement(toTagName(recordName)), addNodes(record));
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
                Element element = document.createElement(toTagName(field));
                Object value = record.get(field);
                if (value != null) {
                    String stringValue = value.toString();
                    element.appendChild(document.createTextNode(stringValue));
                }
                container.appendChild(element);
                return container;
            }
        };
    }

    private String toTagName(Keyword recordName) {
        String xpath = recordName.toString();
        String[] parts = xpath.split("/");

        return parts[parts.length - 1];
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        throw new UnsupportedOperationException();
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        throw new UnsupportedOperationException();
    }

    public Number remove(Keyword recordName) {
        return Xml.remove(document, recordName.toString()).size();
    }
}
