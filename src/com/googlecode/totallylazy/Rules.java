package com.googlecode.totallylazy;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Callables.callWith;
import static com.googlecode.totallylazy.Rule.rule;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Rules<A, B> implements Function1<A, B>, Predicate<A>, Value<Sequence<Rule<A,B>>> {
    private final Deque<Rule<A, B>> rules = new ArrayDeque<Rule<A, B>>();

    private Rules(Sequence<Rule<A, B>> rules) {
        this.rules.addAll(rules.toList());
    }

    @Deprecated
    public Rules<A, B> add(final Predicate<? super A> predicate, final Callable1<? super A, ? extends B> callable) {
        return addFirst(predicate, callable);
    }

    @Deprecated
    public Rules<A, B> add(Rule<? super A, ? extends B> rule) {
        addFirst(Unchecked.<Rule<A, B>>cast(rule));
        return this;
    }

    public Rules<A, B> addFirst(final Predicate<? super A> predicate, final Callable1<? super A, ? extends B> callable) {
        return addFirst(rule(predicate, callable));
    }

    public Rules<A, B> addFirst(Rule<? super A, ? extends B> rule) {
        rules.addFirst(Unchecked.<Rule<A, B>>cast(rule));
        return this;
    }

    public Rules<A, B> addLast(final Predicate<? super A> predicate, final Callable1<? super A, ? extends B> callable) {
        return addLast(rule(predicate, callable));
    }

    public Rules<A, B> addLast(Rule<? super A, ? extends B> rule) {
        rules.addLast(Unchecked.<Rule<A, B>>cast(rule));
        return this;
    }

    public Sequence<Rule<A,B>> value() {
        return sequence(rules).realise();
    }

    @Override
    public boolean matches(final A other) {
        return sequence(rules).exists(Predicates.matches(other));
    }

    @Override
    public B call(final A instance) throws Exception {
        return find(instance).get().call(instance);
    }

    public Option<Rule<A, B>> find(A instance) {
        return filter(instance).headOption();
    }

    public Sequence<Rule<A, B>> filter(A instance) {
        return sequence(rules).
                filter(Predicates.matches(instance));
    }

    public Sequence<B> applyMatching(A instance) {
        return filter(instance).map(Callables.<A,B>callWith(instance));
    }

    public static <A, B> Rules<A, B> rules() {
        return new Rules<A, B>(Sequences.<Rule<A, B>>empty());
    }

    public static <A, B> Rules<A, B> rules(Sequence<Rule<A, B>> rules) {
        return new Rules<A, B>(rules);
    }

}