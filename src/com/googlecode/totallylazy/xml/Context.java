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
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;
import static com.googlecode.totallylazy.xml.StreamingXPath.child;
import static com.googlecode.totallylazy.xml.StreamingXPath.xpath;

public class Context {
    private final PersistentList<XMLEvent> path;
    private final Sequence<XMLEvent> remainder;

    public Context(Sequence<XMLEvent> remainder) {
        this(remainder, empty());
    }

    public Context(Sequence<XMLEvent> remainder, PersistentList<XMLEvent> path) {
        this.remainder = remainder;
        this.path = path;
    }

    public Context add(XMLEvent value) {
        return new Context(remainder.tail(),
                path.headOption().is(instanceOf(Characters.class)) ? path.tail().cons(value) : path.cons(value));
    }

    public Context remove() {
        return new Context(remainder.tail(),
                path.headOption().is(instanceOf(Characters.class)) ? path.tail().tail() : path.tail());
    }

    public Context skip() {
        return new Context(remainder.tail(), path);
    }

    @Override
    public String toString() {
        return Sequences.toString(path(), "/");
    }

    public Option<XMLEvent> current() {
        return path.headOption();
    }

    public PersistentList<XMLEvent> path() {return reverse(path);}

    public Sequence<XMLEvent> remainder() {
        return remainder;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public Option<Context> next() {
        if (remainder.isEmpty()) return none(Context.class);
        XMLEvent head = remainder.head();
        if (head instanceof StartElement) return some(add(head));
        if (head instanceof Characters) return some(add(head));
        if (head instanceof EndElement) {
            if(path.isEmpty()) return none();
            return remove().next();
        }
        return skip().next();
    }

    public Sequence<Context> relative() {
        return XmlReader.locations(new Context(remainder));
    }

    public String name() {
        XMLEvent head = path.head();
        return ((StartElement) head).getName().getLocalPart();
    }

    public String text() {
        XMLEvent head = path.head();
        if(head instanceof Characters) return ((Characters) head).getData();
        return relative().filter(xpath(child(StreamingXPath.text()))).toString("");
    }
}
