package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Xml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.forwardOnly;

public class NodeCreator implements Callable1<Location, Node> {
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
            if (event instanceof Characters)
                parent.appendChild(parent.getOwnerDocument().createTextNode(((Characters) event).getData()));
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
