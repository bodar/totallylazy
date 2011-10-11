package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;

import java.net.URI;

import static com.googlecode.totallylazy.URLs.uri;

public class UriMapping extends AbstractStringMapping<URI> {
    public UriMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    public String toString(URI value) throws Exception {
        return value.toString();
    }

    @Override
    protected URI fromString(String value) throws Exception {
        return uri(value);
    }

}
