package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Rules;
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
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.StreamingXPath.name;

public class XmlReader {
    private final XMLEventReader reader;
    public XmlReader(XMLEventReader reader) {
        this.reader = reader;
    }

    public static XmlReader xmlReader(Reader reader) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return new XmlReader(factory.createXMLEventReader(reader));
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

    public StatefulIterator<Location> iterator(Predicate<? super Location> predicate) {
        return iterator((Location location) -> predicate.matches(location) ? some(location) : none());
    }

    public <T> StatefulIterator<T> iterator(Rules<? super Location, ? extends T> rules) {
        return iterator((Location location) -> rules.find(location).map(rule -> rule.call(location)));
    }

    public <T> StatefulIterator<T> iterator(Callable1<? super Location, ? extends Option<T>> callable) {
        return new StatefulIterator<T>() {
            private Location path = new Location(reader);
            @Override
            protected T getNext() throws Exception {
                while (reader.hasNext()) {
                    XMLEvent event = reader.nextEvent();
                    if (event instanceof EndElement) {
                        if(path.isEmpty()) return finished();
                        path = path.remove();
                    }
                    if (event instanceof StartElement) {
                        StartElement start = (StartElement) event;
                        path = path.add(start);
                        Option<T> option = callable.call(path);
                        if (option.isDefined()) {
                            path = path.remove();
                            return option.get();
                        }
                    }
                }
                return finished();
            }
        };
    }

}
