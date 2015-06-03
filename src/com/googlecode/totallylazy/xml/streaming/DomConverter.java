package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;

import static com.googlecode.totallylazy.xml.streaming.XPath.node;
import static com.googlecode.totallylazy.xml.streaming.XPath.xpath;

public class DomConverter {
    public static Node convert(Context context) throws Exception {
        return children(copyAttributes(context, element(context.name())), context.relative());
    }

    static Element element(String name) {
        return Xml.document("<" + name + "/>").getDocumentElement();
    }

    static Node children(Node parent, Sequence<Context> contexts) throws XMLStreamException {
        if(contexts.isEmpty()) return parent;
        for (Context child : contexts.filter(xpath(XPath.child(node())))) {
            if(child.isText()) parent.appendChild(parent.getOwnerDocument().createTextNode(child.text()));
            if(child.isElement()) children(child(parent, child), child.relative());
        }
        return parent;
    }

    static Node child(Node parent, Context start) {
        return parent.appendChild(copyAttributes(start, parent.getOwnerDocument().createElement(start.name())));
    }

    static Element copyAttributes(Context source, Element destination) {
        for (Pair<String, String> attribute : source.attributes()) {
            destination.setAttribute(attribute.getKey(), attribute.getValue());
        }
        return destination;
    }
}
