package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unary;
import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
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
        return logicalPredicate((Node node) -> predicate.matches(node.attributes().get(name)));
    }

    public static Callable1<PersistentList<Node>, Option<PersistentList<Node>>> descendant(String name) {
        return descendant(name(name));
    }

    public static Callable1<PersistentList<Node>, Option<PersistentList<Node>>> descendant(Predicate<? super Node> predicate) {
        return steps -> descendant(predicate, steps);
    }

    private static Option<PersistentList<Node>> descendant(Predicate<? super Node> predicate, PersistentList<Node> steps) {
        return steps.tails().
                filter(tail -> predicate.matches(tail.head())).
                lastOption().
                map(PersistentList::tail);

    }

    public static Callable1<PersistentList<Node>, Option<PersistentList<Node>>> child(String name) {
        return child(name(name));
    }

    public static Callable1<PersistentList<Node>, Option<PersistentList<Node>>> child(Predicate<? super Node> predicate) {
        return steps -> {
            if (steps.headOption().is(predicate)) return some(steps.tail());
            return none();
        };
    }

    public static LogicalPredicate<Node> text() {
        return logicalPredicate(Node::isText);
    }

    @SafeVarargs
    public static Predicate<Context> xpath(Callable1<? super PersistentList<Node>, ? extends Option<PersistentList<Node>>>... steps) {
        return context -> sequence(steps).
                fold(some(context.path()), Option::flatMap).
                contains(PersistentList.constructors.empty());
    }

    public static Predicate<Node> node() {
        return Predicates.any();
    }
}
