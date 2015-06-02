package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unary;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.Compose.compose;
import static com.googlecode.totallylazy.predicates.LogicalPredicate.logicalPredicate;

public class StreamingXPath {
    public static LogicalPredicate<XMLEvent> name(String value) {
        return name(value.equals("*") ? Predicates.any() : is(value));
    }

    public static LogicalPredicate<XMLEvent> name(Predicate<? super String> predicate) {
        return instanceOf(StartElement.class, element ->
                predicate.matches(element.getName().getLocalPart()));
    }

    public static LogicalPredicate<XMLEvent> attribute(String name, Predicate<? super String> predicate) {
        return instanceOf(StartElement.class, element ->
                predicate.matches(element.getAttributeByName(new QName(name)).getValue()));
    }

    public static Unary<PersistentList<XMLEvent>> descendant(Predicate<? super XMLEvent> predicate) {
        return steps -> descendant(predicate, steps);
    }

    private static PersistentList<XMLEvent> descendant(Predicate<? super XMLEvent> predicate, PersistentList<XMLEvent> steps){
        return steps.tails().
                filter(tail -> predicate.matches(tail.head())).
                lastOption().
                map(PersistentList::tail).
                getOrThrow(new NoSuchElementException());
    }

    public static Unary<PersistentList<XMLEvent>> child(Predicate<? super XMLEvent> predicate) {
        return steps -> {
            if(steps.headOption().is(predicate)) return steps.tail();
            throw new NoSuchElementException();
        };
    }

    public static LogicalPredicate<XMLEvent> text() {
        return instanceOf(Characters.class);
    }

    @SafeVarargs
    public static Predicate<Context> xpath(Callable1<? super PersistentList<XMLEvent>, ? extends PersistentList<XMLEvent>>... steps) {
        return context -> {
            try {
                return sequence(steps).
                        map(Functions::function).
                        reduce(compose()).
                        call(context.path()).
                        isEmpty();
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static Predicate<XMLEvent> node() {
        return Predicates.any();
    }
}
