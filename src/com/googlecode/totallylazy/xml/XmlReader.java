package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Rules;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.util.Iterator;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.StreamingXPath.name;

public class XmlReader<T> extends StatefulIterator<T> {
    private final XMLEventReader reader;
    private final Predicate<Location> predicate;
    private final Callable2<Location, XMLEventReader, T> function;
    private final Location path = new Location();

    private XmlReader(XMLEventReader reader, Predicate<Location> predicate, Callable2<Location, XMLEventReader, T> function) {
        this.reader = reader;
        this.predicate = predicate;
        this.function = function;
    }

    public static XmlReader<Node> xmlReader(Reader reader, String localName) throws LazyException {
        return xmlReader(reader, descendant(name(localName)));
    }

    public static XmlReader<Node> xmlReader(Reader reader, Predicate<Location> predicate) {
            return xmlReader(reader, predicate, (location, xmlEventReader) -> new NodeCreator(xmlEventReader).call(location));
    }

    public static <T> XmlReader<T> xmlReader(Reader reader, Predicate<Location> predicate, Callable2<Location, XMLEventReader, T> function) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return new XmlReader<>(factory.createXMLEventReader(reader), predicate, function);
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    @Override
    protected T getNext() throws Exception {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event instanceof EndElement) path.remove();
            if (event instanceof StartElement) {
                StartElement start = (StartElement) event;
                path.add(start);
                if (predicate.matches(path)) {
                    T t = function.call(path, reader);
                    path.remove();
                    return t;
                }
            }
        }
        return finished();
    }

    private static class NodeCreator implements Callable1<Location, Node> {
        private final XMLEventReader reader;

        public NodeCreator(XMLEventReader reader) {
            this.reader = reader;
        }

        @Override
        public Node call(Location location) throws Exception {
            StartElement start = location.current();
            return children(copyAttributes(start, element(start.getName().getLocalPart())));
        }

        private Element element(String name) {
            return Xml.document("<" + name + "/>").getDocumentElement();
        }

        private Node children(Node parent) throws XMLStreamException {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event instanceof EndElement) return parent;
                if (event instanceof Characters) parent.appendChild(parent.getOwnerDocument().createTextNode(((Characters) event).getData()));
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


    }
}
