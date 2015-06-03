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
import java.io.StringReader;
import java.util.Iterator;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.xml.streaming.XPath.descendant;
import static com.googlecode.totallylazy.xml.streaming.XPath.name;
import static com.googlecode.totallylazy.xml.streaming.XPath.xpath;

public class Xml {
    private static XMLEventReader xmlEventReader(Reader reader) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_COALESCING, true);
            return factory.createXMLEventReader(reader);
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    public static Sequence<Node> nodes(Reader reader, String localName) throws LazyException {
        return nodes(reader, xpath(descendant(name(localName))));
    }

    public static Sequence<Node> nodes(Reader reader, Predicate<Context> predicate) {
        return contexts(reader).filter(predicate).map(DomConverter::convert);
    }

    public static Sequence<XMLEvent> events(Reader reader) {
        return Computation.memoize(Unchecked.<Iterator<XMLEvent>>cast(xmlEventReader(reader)));
    }

    public static Sequence<Context> contexts(String xml) {
        return contexts(new StringReader(xml));
    }

    public static Sequence<Context> contexts(Reader reader) {
        return contexts(new Context(events(reader)).next().get());
    }

    public static Computation<Context> contexts(Context context) {
        return Computation.compute(context, Context::next);
    }
}
