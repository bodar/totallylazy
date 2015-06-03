package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Computation;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.util.Iterator;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.name;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.xpath;

public class XmlReader {
    private static XMLEventReader xmlEventReader(Reader reader) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_COALESCING, true);
            return factory.createXMLEventReader(reader);
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    public static Sequence<Node> xmlReader(Reader reader, String localName) throws LazyException {
        return xmlReader(reader, xpath(descendant(name(localName))));
    }

    public static Sequence<Node> xmlReader(Reader reader, Predicate<Context> predicate) {
        return Context.contexts(reader).filter(predicate).map(
                location -> new NodeCreator().call(location));
    }

    public static Sequence<XMLEvent> xmlEvents(Reader reader) {
        return Computation.memoize(Unchecked.<Iterator<XMLEvent>>cast(xmlEventReader(reader)));
    }
}
