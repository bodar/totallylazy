package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Sequences;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.ArrayDeque;

public class Location {
    private final ArrayDeque<StartElement> path;
    private final XMLEventReader xmlReader;

    public Location(XMLEventReader xmlReader) {
        this(xmlReader, new ArrayDeque<>());
    }

    public Location(XMLEventReader xmlReader, ArrayDeque<StartElement> path) {
        this.xmlReader = xmlReader;
        this.path = path;
    }

    public Location add(StartElement value) {
        path.addLast(value);
        return this;
    }

    public Location remove() {
        path.removeLast();
        return this;
    }

    public StartElement current() {
        return path.getLast();
    }

    @Override
    public String toString() {
        return Sequences.toString(path, "/");
    }

    public XMLEventReader reader() {
        return xmlReader;
    }

    public Location clone() {
        return new Location(xmlReader, path.clone());
    }

}
