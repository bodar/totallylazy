package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentMap;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.collections.PersistentMap.constructors.emptyMap;
import static com.googlecode.totallylazy.collections.PersistentMap.constructors.map;

public interface XmlNode {
    default String name() { throw new UnsupportedOperationException(); }

    default String text() { throw new UnsupportedOperationException(); }

    default PersistentMap<String, String> attributes() { return emptyMap(); }

    default boolean isText() { return false; }

    default boolean isElement() { return false; }

    static XmlNode element(StartElement startElement){
        PersistentMap<String, String> attributes = map(forwardOnly(Unchecked.<Iterator<Attribute>>cast(startElement.getAttributes())).
                map((Attribute attribute) -> Pair.pair(attribute.getName().getLocalPart(), attribute.getValue())));
        return element(startElement.getName().getLocalPart(), attributes);
    }

    static XmlNode element(String name, PersistentMap<String, String> attributes){
        return new XmlNode() {
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
        };
    }

    static XmlNode text(Characters value){
        return text(value.getData());
    }

    static XmlNode text(String value){
        return new XmlNode() {
            @Override
            public String text() { return value; }

            @Override
            public boolean isText() { return true; }

            @Override
            public String toString() {
                return text();
            }
        };
    }
}
