package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Sequences;

import javax.xml.stream.events.StartElement;
import java.util.ArrayDeque;

public class StreamingPath {
    private final ArrayDeque<StartElement> path = new ArrayDeque<>();

    public StreamingPath add(StartElement value) {
        path.addLast(value);
        return this;
    }

    public StreamingPath remove() {
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
}
