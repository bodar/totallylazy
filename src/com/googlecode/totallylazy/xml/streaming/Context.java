package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.collections.PersistentMap;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;

public class Context implements Node {
    private final PersistentList<Node> path;
    private final Sequence<XMLEvent> remainder;

    public Context(Sequence<XMLEvent> remainder) {
        this(remainder, empty());
    }

    public Context(Sequence<XMLEvent> remainder, PersistentList<Node> path) {
        this.remainder = remainder;
        this.path = path;
    }

    public Context push(Node node) {
        return new Context(remainder.tail(),
                path.headOption().is(Node::isText) ?
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

    public Option<Node> current() {
        return path.headOption();
    }

    public PersistentList<Node> path() {return reverse(path);}

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public Option<Context> next() {
        if (remainder.isEmpty()) return none(Context.class);
        XMLEvent head = remainder.head();
        if (head instanceof StartElement) return some(push(Element.element((StartElement) head)));
        if (head instanceof Characters) return some(push(Text.text((Characters) head)));
        if (head instanceof EndElement) return pop(((EndElement) head).getName().getLocalPart());
        return skip().next();
    }

    private Option<Context> pop(String name) {
        return path.tails().
                find(path -> path.headOption().is(XPath.name(name))).
                flatMap(newPath -> new Context(remainder.tail(), newPath.tail()).next());
    }

    public Sequence<Context> relative() {
        return Xml.contexts(new Context(remainder)).tail();
    }

    @Override
    public String name() {
        return path.head().name();
    }

    @Override
    public String text() {
        if(isText()) return path.head().text();
        return relative().
                filter(XPath.xpath(XPath.descendant(XPath.text()))).
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
