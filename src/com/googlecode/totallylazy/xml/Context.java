package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Computation;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.collections.PersistentMap;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.Reader;
import java.util.Iterator;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.Sequences.memoize;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
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

    public static Sequence<Context> contexts(Reader reader) {
        return contexts(new Context(XmlReader.xmlEvents(reader)).next().get());
    }

    public static Computation<Context> contexts(Context context) {
        return Computation.compute(context, Context::next);
    }

    public Context push() {
        return new Context(remainder.tail(),
                path.headOption().is(instanceOf(Characters.class)) ?
                        path.tail().cons(remainder.head()) :
                        path.cons(remainder.head()));
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
        if (head instanceof StartElement) return some(push());
        if (head instanceof Characters) return some(push());
        if (head instanceof EndElement) return pop(((EndElement) head).getName().getLocalPart());
        return skip().next();
    }

    private Option<Context> pop(String name) {
        return path.tails().
                find(path -> path.headOption().is(StreamingXPath.name(name))).
                flatMap(newPath -> new Context(remainder.tail(), newPath.tail()).next());
    }

    public Sequence<Context> relative() {
        return contexts(new Context(remainder)).tail();
    }

    public String name() {
        return startElement().getName().getLocalPart();
    }

    private StartElement startElement() {return (StartElement) path.head();}

    public String text() {
        if(isText()) return ((Characters) path.head()).getData();
        return relative().
                filter(xpath(descendant(StreamingXPath.text()))).
                map(Context::text).
                toString("");
    }

    public boolean isText() {
        return path.headOption().is(instanceOf(Characters.class));
    }

    public boolean isElement() {
        return path.headOption().is(instanceOf(StartElement.class));
    }

    public PersistentMap<String, String> attributes() {
        return PersistentMap.constructors.map(forwardOnly(Unchecked.<Iterator<Attribute>>cast(startElement().getAttributes())).
                map((Attribute attribute) -> Pair.pair(attribute.getName().getLocalPart(), attribute.getValue())));
    }
}
