package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.PersistentList;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;

public class LocationPath {
    private final PersistentList<StartElement> path;
    private final Sequence<XMLEvent> remainder;

    public LocationPath(Sequence<XMLEvent> remainder) {
        this(remainder, empty());
    }

    public LocationPath(Sequence<XMLEvent> remainder, PersistentList<StartElement> path) {
        this.remainder = remainder;
        this.path = path;
    }

    public LocationPath add(StartElement value) {
        return new LocationPath(remainder.tail(), path.cons(value));
    }

    public LocationPath remove() {
        return new LocationPath(remainder.tail(), path.tail());
    }

    public LocationPath skip() {
        return new LocationPath(remainder.tail(), path);
    }

    @Override
    public String toString() {
        return Sequences.toString(steps(), "/") + remainder.headOption();
    }

    public Option<StartElement> current() {
        return path.headOption();
    }

    public PersistentList<StartElement> steps() {return reverse(path);}

    public Sequence<XMLEvent> remainder() {
        return remainder;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public Option<LocationPath> next() {
        if (remainder.isEmpty()) return none(LocationPath.class);
        XMLEvent head = remainder.head();
        if (head instanceof StartElement) return some(add((StartElement) head));
        if (head instanceof Characters) return some(skip());
        if (head instanceof EndElement) {
            if(path.isEmpty()) return none();
            return some(remove());
        }
        return skip().next();
    }

    public Sequence<LocationPath> paths() {
        return XmlReader.locations(new LocationPath(remainder));
    }
}
