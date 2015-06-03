package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;

public class NodeCreator implements Callable1<Context, Node> {
    @Override
    public Node call(Context context) throws Exception {
        return children(copyAttributes(context, element(context.name())), context.relative());
    }

    private Element element(String name) {
        return Xml.document("<" + name + "/>").getDocumentElement();
    }

    private Node children(Node parent, Sequence<Context> contexts) throws XMLStreamException {
        if(contexts.isEmpty()) return parent;
        for (Context child : contexts.filter(StreamingXPath.xpath(StreamingXPath.child(StreamingXPath.node())))) {
            if(child.isText()) parent.appendChild(parent.getOwnerDocument().createTextNode(child.text()));
            if(child.isElement()) children(child(parent, child), child.relative());
        }
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
