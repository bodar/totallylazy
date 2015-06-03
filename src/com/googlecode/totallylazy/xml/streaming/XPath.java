package com.googlecode.totallylazy.xml.streaming;

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

public class XPath {
    public static LogicalPredicate<Node> name(String value) {
        return name(value.equals("*") ? Predicates.any() : is(value));
    }

    public static LogicalPredicate<Node> name(Predicate<? super String> predicate) {
        return logicalPredicate((Node element) -> element.isElement() && predicate.matches(element.name()));
    }

    public static LogicalPredicate<Node> attribute(String name, Predicate<? super String> predicate) {
        return logicalPredicate((Node node) -> {
            String value = node.attributes().get(name);
            return predicate.matches(value);
        });
    }

    public static Unary<PersistentList<Node>> descendant(Predicate<? super Node> predicate) {
        return steps -> descendant(predicate, steps);
    }

    private static PersistentList<Node> descendant(Predicate<? super Node> predicate, PersistentList<Node> steps){
        return steps.tails().
                filter(tail -> predicate.matches(tail.head())).
                lastOption().
                map(PersistentList::tail).
                getOrThrow(new NoSuchElementException());
    }

    public static Unary<PersistentList<Node>> child(Predicate<? super Node> predicate) {
        return steps -> {
            if(steps.headOption().is(predicate)) return steps.tail();
            throw new NoSuchElementException();
        };
    }

    public static LogicalPredicate<Node> text() {
        return logicalPredicate(Node::isText);
    }

    @SafeVarargs
    public static Predicate<Context> xpath(Callable1<? super PersistentList<Node>, ? extends PersistentList<Node>>... steps) {
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

    public static Predicate<Node> node() {
        return Predicates.any();
    }
}
