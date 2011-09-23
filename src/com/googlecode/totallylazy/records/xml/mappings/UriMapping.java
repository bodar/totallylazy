package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.net.URI;

import static com.googlecode.totallylazy.URLs.uri;

public class UriMapping implements Mapping<URI> {
    private final StringMapping mapping = new StringMapping();

    public Sequence<Node> to(Document document, String expression, URI value) throws Exception {
        return mapping.to(document, expression, value.toString());
    }

    public URI from(Sequence<Node> nodes) throws Exception {
        return uri(mapping.from(nodes));
    }
}
