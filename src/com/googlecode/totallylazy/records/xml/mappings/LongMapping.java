package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class LongMapping implements Mapping<Long> {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, Long value) {
        return mapping.to(document, expression, Long.toString(value));
    }

    public Long from(Sequence<Node> nodes) {
        return Long.valueOf(mapping.from(nodes));
    }
}
