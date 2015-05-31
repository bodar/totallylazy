package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.PersistentList;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.ArrayDeque;

import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;

public class Location {
    private final PersistentList<StartElement> path;
    private final XMLEventReader xmlReader;

    public Location(XMLEventReader xmlReader) {
        this(xmlReader, empty());
    }

    public Location(XMLEventReader xmlReader, PersistentList<StartElement> path) {
        this.xmlReader = xmlReader;
        this.path = path;
    }

    public Location add(StartElement value) {
        return new Location(xmlReader, path.cons(value));
    }

    public Location remove() {
        return new Location(xmlReader, path.tail());
    }

    public StartElement current() {
        return path.head();
    }

    @Override
    public String toString() {
        return Sequences.toString(reverse(path), "/");
    }

    public XMLEventReader reader() {
        return xmlReader;
    }

    public XmlReader stream() {
        return new XmlReader(xmlReader);
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }
}
