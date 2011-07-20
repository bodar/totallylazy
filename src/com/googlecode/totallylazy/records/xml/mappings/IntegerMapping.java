package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class IntegerMapping implements Mapping<Integer>  {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, Integer value) {
        return mapping.to(document, expression, Integer.toString(value));
    }

    public Integer from(Sequence<Node> nodes) {
        return Integer.valueOf(mapping.from(nodes));
    }
}
