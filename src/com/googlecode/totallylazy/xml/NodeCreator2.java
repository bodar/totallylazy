package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
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

public class NodeCreator2 implements Callable1<Context, Node> {
    @Override
    public Node call(Context context) throws Exception {
        return children(copyAttributes(context, element(context.name())), context.relative());
    }

    private Element element(String name) {
        return Xml.document("<" + name + "/>").getDocumentElement();
    }

    private Node children(Node parent, Sequence<Context> contexts) throws XMLStreamException {
        if(contexts.isEmpty()) return parent;
        Context child = contexts.head();
        if(child.isText()) parent.appendChild(parent.getOwnerDocument().createTextNode(child.text()));
        if(child.isElement()) children(child(parent, child), child.relative());
        return parent;
    }

    private Node child(Node parent, Context start) {
        return parent.appendChild(copyAttributes(start, parent.getOwnerDocument().createElement(start.name())));
    }

    private Element copyAttributes(Context source, Element destination) {
        for (Pair<String, String> attribute : source.attributes()) {
            destination.setAttribute(attribute.getKey(), attribute.getValue());
        }
        return destination;
    }
}
