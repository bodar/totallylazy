package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import static com.googlecode.totallylazy.records.xml.Xml.load;
import static com.googlecode.totallylazy.records.xml.Xml.xpath;

public class XmlRecords implements Records {
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

    public void define(Keyword recordName, Keyword<?>... fields) {
        throw new UnsupportedOperationException();
    }

    public boolean exists(Keyword recordName) {
        throw new UnsupportedOperationException();
    }

    public Number add(Keyword recordName, Record... records) {
        throw new UnsupportedOperationException();
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        throw new UnsupportedOperationException();
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        throw new UnsupportedOperationException();
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        throw new UnsupportedOperationException();
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        throw new UnsupportedOperationException();
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        throw new UnsupportedOperationException();
    }

    public Number remove(Keyword recordName) {
        throw new UnsupportedOperationException();
    }
}
