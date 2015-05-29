package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.util.Iterator;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.StreamingXPath.name;

public class XmlReader<T> {
    private final XMLEventReader reader;
    private XmlReader(XMLEventReader reader) {
        this.reader = reader;
    }

    public static <T> XmlReader<T> xmlReader(Reader reader) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return new XmlReader<>(factory.createXMLEventReader(reader));
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    public static Iterator<Node> xmlReader(Reader reader, String localName) throws LazyException {
        return xmlReader(reader, descendant(name(localName)));
    }

    public static Iterator<Node> xmlReader(Reader reader, Predicate<Location> predicate) {
        return Iterators.map(xmlReader(reader).iterator(predicate),
                location -> new NodeCreator(location.reader()).call(location));
    }

    public StatefulIterator<Location> iterator(Predicate<Location> predicate) {
        return new StatefulIterator<Location>() {
            private final Location path = new Location(reader);
            @Override
            protected Location getNext() throws Exception {
                while (reader.hasNext()) {
                    XMLEvent event = reader.nextEvent();
                    if (event instanceof EndElement) path.remove();
                    if (event instanceof StartElement) {
                        StartElement start = (StartElement) event;
                        path.add(start);
                        if (predicate.matches(path)) {
                            Location result = path.clone();
                            path.remove();
                            return result;
                        }
                    }
                }
                return finished();
            }
        };
    }

}
