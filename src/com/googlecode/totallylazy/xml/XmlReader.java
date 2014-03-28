package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.util.Iterator;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.forwardOnly;

public class XmlReader extends StatefulIterator<Node> {
    private final XMLEventReader reader;
    private final Predicate<? super QName> predicate;

    private XmlReader(XMLEventReader reader, Predicate<? super QName> predicate) {
        this.reader = reader;
        this.predicate = predicate;
    }

    public static XmlReader xmlReader(Reader reader, String localName) throws LazyException {
        return xmlReader(reader, where(functions.localPart, is(localName)));
    }

    public static XmlReader xmlReader(Reader reader, Predicate<? super QName> predicate) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return new XmlReader(factory.createXMLEventReader(reader), predicate);
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    @Override
    protected Node getNext() throws Exception {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event instanceof StartElement) {
                StartElement start = (StartElement) event;
                QName qName = start.getName();
                if (predicate.matches(qName)) return children(copyAttributes(start, element(qName.getLocalPart())));
            }
        }
        return finished();
    }

    private Element element(String name) {
        return Xml.document("<" + name + "/>").getDocumentElement();
    }

    private Node children(Node parent) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event instanceof EndElement) return parent;
            if (event instanceof StartElement) children(child(parent, (StartElement) event));
        }
        return parent;
    }

    private Node child(Node parent, StartElement start) {
        return parent.appendChild(copyAttributes(start, parent.getOwnerDocument().createElement(start.getName().getLocalPart())));
    }

    private Element copyAttributes(StartElement source, Element destination) {
        for (Attribute attribute : forwardOnly(Unchecked.<Iterator<Attribute>>cast(source.getAttributes()))) {
            destination.setAttribute(attribute.getName().getLocalPart(), attribute.getValue());
        }
        return destination;
    }

    public static class functions {
        public static Function<QName, String> localPart = new Function<QName, String>() {
            @Override
            public String call(QName qName) throws Exception {
                return qName.getLocalPart();
            }
        };

    }

}
