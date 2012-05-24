package com.googlecode.totallylazy;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Rule.rule;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Rules<A, B> extends Function1<A, B> implements Predicate<A> {
    private final Deque<Rule<A, B>> rules = new ArrayDeque<Rule<A, B>>();

    private Rules(Sequence<Rule<A, B>> rules) {
        this.rules.addAll(rules.toList());
    }

    public Rules<A, B> add(final Predicate<? super A> predicate, final Callable1<? super A, ? extends B> callable) {
        return add(rule(predicate, callable));
    }

    public Rules<A, B> add(Rule<? super A, ? extends B> rule) {
        rules.addFirst(Unchecked.<Rule<A, B>>cast(rule));
        return this;
    }

    @Override
    public boolean matches(final A other) {
        return sequence(rules).exists(Predicates.matches(other));
    }

    @Override
    public B call(final A instance) throws Exception {
        return find(instance).call(instance);
    }

    public Rule<A, B> find(A instance) {
        return filter(instance).head();
    }

    public Sequence<Rule<A, B>> filter(A instance) {
        return sequence(rules).
                filter(Predicates.matches(instance));
    }

    public static <A, B> Rules<A, B> rules() {
        return new Rules<A, B>(Sequences.<Rule<A, B>>empty());
    }

    public static <A, B> Rules<A, B> rules(Sequence<Rule<A, B>> rules) {
        return new Rules<A, B>(rules);
    }

}
