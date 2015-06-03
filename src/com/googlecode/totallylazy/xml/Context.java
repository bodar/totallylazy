package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Computation;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.collections.PersistentMap;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.StreamingXPath.xpath;

public class Context implements XmlNode {
    private final PersistentList<XmlNode> path;
    private final Sequence<XMLEvent> remainder;

    public Context(Sequence<XMLEvent> remainder) {
        this(remainder, empty());
    }

    public Context(Sequence<XMLEvent> remainder, PersistentList<XmlNode> path) {
        this.remainder = remainder;
        this.path = path;
    }

    public static Sequence<Context> contexts(Reader reader) {
        return contexts(new Context(XmlReader.xmlEvents(reader)).next().get());
    }

    public static Computation<Context> contexts(Context context) {
        return Computation.compute(context, Context::next);
    }

    public Context push(XmlNode node) {
        return new Context(remainder.tail(),
                path.headOption().is(XmlNode::isText) ?
                        path.tail().cons(node) :
                        path.cons(node));
    }

    public Context skip() {
        return new Context(remainder.tail(), path);
    }

    @Override
    public String toString() {
        return Sequences.toString(path(), "/");
    }

    public Option<XmlNode> current() {
        return path.headOption();
    }

    public PersistentList<XmlNode> path() {return reverse(path);}

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public Option<Context> next() {
        if (remainder.isEmpty()) return none(Context.class);
        XMLEvent head = remainder.head();
        if (head instanceof StartElement) return some(push(XmlNode.element((StartElement) head)));
        if (head instanceof Characters) return some(push(XmlNode.text((Characters) head)));
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

    @Override
    public String name() {
        return path.head().name();
    }

    @Override
    public String text() {
        if(isText()) return path.head().text();
        return relative().
                filter(xpath(descendant(StreamingXPath.text()))).
                map(Context::text).
                toString("");
    }

    @Override
    public boolean isText() {
        return path.head().isText();
    }

    @Override
    public boolean isElement() {
        return path.head().isElement();
    }

    @Override
    public PersistentMap<String, String> attributes() {
        return path.head().attributes();
    }
}
