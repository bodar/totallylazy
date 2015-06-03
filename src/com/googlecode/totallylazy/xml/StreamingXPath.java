package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unary;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.Compose.compose;
import static com.googlecode.totallylazy.predicates.LogicalPredicate.logicalPredicate;

public class StreamingXPath {
    public static LogicalPredicate<XmlNode> name(String value) {
        return name(value.equals("*") ? Predicates.any() : is(value));
    }

    public static LogicalPredicate<XmlNode> name(Predicate<? super String> predicate) {
        return logicalPredicate((XmlNode element) -> element.isElement() && predicate.matches(element.name()));
    }

    public static LogicalPredicate<XmlNode> attribute(String name, Predicate<? super String> predicate) {
        return logicalPredicate( (XmlNode node) -> {
            String value = node.attributes().get(name);
            return predicate.matches(value);
        });
    }

    public static Unary<PersistentList<XmlNode>> descendant(Predicate<? super XmlNode> predicate) {
        return steps -> descendant(predicate, steps);
    }

    private static PersistentList<XmlNode> descendant(Predicate<? super XmlNode> predicate, PersistentList<XmlNode> steps){
        return steps.tails().
                filter(tail -> predicate.matches(tail.head())).
                lastOption().
                map(PersistentList::tail).
                getOrThrow(new NoSuchElementException());
    }

    public static Unary<PersistentList<XmlNode>> child(Predicate<? super XmlNode> predicate) {
        return steps -> {
            if(steps.headOption().is(predicate)) return steps.tail();
            throw new NoSuchElementException();
        };
    }

    public static LogicalPredicate<XmlNode> text() {
        return logicalPredicate(XmlNode::isText);
    }

    @SafeVarargs
    public static Predicate<Context> xpath(Callable1<? super PersistentList<XmlNode>, ? extends PersistentList<XmlNode>>... steps) {
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

    public static Predicate<XmlNode> node() {
        return Predicates.any();
    }
}
