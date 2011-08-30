package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static com.googlecode.totallylazy.records.xml.Xml.contents;
import static com.googlecode.totallylazy.records.xml.XmlRecords.toTagName;

public class StringMapping implements Mapping<String> {
    public Sequence<Node> to(Document document, String expression, String value) {
        Element element = document.createElement(toTagName(expression));
        element.appendChild(document.createTextNode(value));
        return Sequences.<Node>sequence(element);
    }

    public String from(Sequence<Node> nodes) {
        return contents(nodes);
    }
}
