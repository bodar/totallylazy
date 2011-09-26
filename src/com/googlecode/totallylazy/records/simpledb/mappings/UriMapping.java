package com.googlecode.totallylazy.records.simpledb.mappings;

import java.net.URI;

import static com.googlecode.totallylazy.URLs.uri;

public class UriMapping implements Mapping<URI>{
    public URI toValue(String value) {
        return uri(value);
    }

    public String toString(URI value) {
        return value.toString();
    }
}
