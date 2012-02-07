package com.googlecode.totallylazy;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Rules<A, B> extends Function1<A, B> implements Predicate<A> {
    private final Deque<Rule<A, B>> rules = new ArrayDeque<Rule<A, B>>();

    public Rules add(final Predicate<? super A> predicate, final Callable1<? super A, ? extends B> callable) {
        rules.addFirst(Rule.<A, B>rule(predicate, callable));
        return this;
    }

    @Override
    public boolean matches(final A other) {
        return sequence(rules).exists(Predicates.matches(other));
    }

    @Override
    public B call(final A instance) throws Exception {
        return sequence(rules).
                filter(Predicates.matches(instance)).
                head().
                call(instance);
    }
}
