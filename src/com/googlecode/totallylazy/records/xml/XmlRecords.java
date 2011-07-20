package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.Reader;

public class XmlRecords implements Records {
    private final Reader reader;
    private final XPath xpath;

    public XmlRecords(Reader reader) {
        xpath = Xml.xpath();
        this.reader = reader;
    }

    public Sequence<Record> get(Keyword recordName) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate(recordName.toString(), new InputSource(reader), XPathConstants.NODESET);
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
