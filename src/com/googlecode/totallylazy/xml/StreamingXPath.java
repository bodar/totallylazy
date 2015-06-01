package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.predicates.LogicalPredicate.logicalPredicate;

public class StreamingXPath {
    public static LogicalPredicate<XMLEvent> name(String value){
        return name(value.equals("*") ? Predicates.any() : is(value));
    }

    public static LogicalPredicate<XMLEvent> name(Predicate<? super String> predicate){
        return instanceOf(StartElement.class, element ->
                predicate.matches(element.getName().getLocalPart()));
    }

    public static LogicalPredicate<XMLEvent> attribute(String name, Predicate<? super String> predicate){
        return instanceOf(StartElement.class, element ->
                predicate.matches(element.getAttributeByName(new QName(name)).getValue()));
    }

    public static Predicate<Location> descendantOld(Predicate<? super StartElement> predicate) {
        return path -> predicate.matches(path.current());
    }

    public static Predicate<Context> descendant(Predicate<? super XMLEvent> predicate) {
        return path -> path.current().is(predicate);
    }

    public static Predicate<Context> child(Predicate<? super XMLEvent> predicate) {
        return context -> {
            PersistentList<XMLEvent> steps = context.path();
            return steps.size() == 1 && predicate.matches(steps.head());
        };
    }

    public static LogicalPredicate<XMLEvent> text() {
        return instanceOf(Characters.class);
    }
}
