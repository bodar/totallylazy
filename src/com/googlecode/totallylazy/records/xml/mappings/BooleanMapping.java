package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class BooleanMapping implements Mapping<Boolean>  {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, Boolean value) {
        return mapping.to(document, expression, Boolean.toString(value));
    }

    public Boolean from(Sequence<Node> nodes) {
        return Boolean.parseBoolean(mapping.from(nodes));
    }
}
