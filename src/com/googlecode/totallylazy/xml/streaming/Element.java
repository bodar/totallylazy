package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentMap;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.collections.PersistentMap.constructors.map;

public class Element implements Node {
    private final String name;
    private final PersistentMap<String, String> attributes;

    private Element(String name, PersistentMap<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public static Element element(StartElement startElement){
        PersistentMap<String, String> attributes = map(forwardOnly(Unchecked.<Iterator<Attribute>>cast(startElement.getAttributes())).
                map((Attribute attribute) -> Pair.pair(attribute.getName().getLocalPart(), attribute.getValue())));
        return element(startElement.getName().getLocalPart(), attributes);
    }

    public static Element element(String name, PersistentMap<String, String> attributes){
        return new Element(name, attributes);
    }

    @Override
    public String name() { return name; }

    @Override
    public PersistentMap<String, String> attributes() { return attributes; }

    @Override
    public boolean isElement() { return true; }

    @Override
    public String toString() {
        return String.format("<%s>", name());
    }
}
