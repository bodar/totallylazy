package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.UUID;

public class UUIDMapping implements Mapping<UUID>  {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, UUID value) {
        return mapping.to(document, expression, value.toString());
    }

    public UUID from(Sequence<Node> nodes) {
        return UUID.fromString(mapping.from(nodes));
    }
}
