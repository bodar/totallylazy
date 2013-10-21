package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.annotations.tailrec;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
import static com.googlecode.totallylazy.Sequences.forwardOnly;

public class XmlReader extends StatefulIterator<Node> {
    private final XMLEventReader reader;
    private final String repeatingElement;

    private XmlReader(XMLEventReader reader, String repeatingElement) {
        this.reader = reader;
        this.repeatingElement = repeatingElement;
    }

    public static XmlReader xmlReader(Reader reader, String repeatingElement) throws LazyException {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return new XmlReader(factory.createXMLEventReader(reader), repeatingElement);
        } catch (XMLStreamException e) {
            throw lazyException(e);
        }
    }

    @Override
    @tailrec
    protected Node getNext() throws Exception {
        if (!reader.hasNext()) return finished();
        XMLEvent event = reader.nextEvent();

        if (event instanceof StartElement) {
            StartElement start = (StartElement) event;
            if (start.getName().getLocalPart().equals(repeatingElement)) {
                Document document = Xml.document("<" + repeatingElement + "/>");
                copyAttributes(start, document.getDocumentElement());
                return children(document.getDocumentElement());
            }
        }
        return getNext();
    }

    private Element children(Element parent) throws XMLStreamException {
        if (!reader.hasNext()) return parent;
        XMLEvent event = reader.nextEvent();
        if (event instanceof EndElement) return parent;

        if (event instanceof StartElement) {
            StartElement start = (StartElement) event;
            Element child = parent.getOwnerDocument().createElement(start.getName().getLocalPart());
            copyAttributes(start, child);
            parent.appendChild(child);
            children(child);
        }
        return children(parent);
    }

    private void copyAttributes(StartElement source, Element destination) {
        for (Attribute attribute : forwardOnly(Unchecked.<Iterator<Attribute>>cast(source.getAttributes()))) {
            destination.setAttribute(attribute.getName().getLocalPart(), attribute.getValue());
        }
    }

}
