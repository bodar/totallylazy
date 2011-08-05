package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ObjectMapping implements Mapping<Object> {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, Object value) throws Exception {
        return mapping.to(document, expression, value.toString());
    }

    public Object from(Sequence<Node> nodes) throws Exception {
        return mapping.from(nodes);
    }
}
