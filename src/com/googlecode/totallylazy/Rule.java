package com.googlecode.totallylazy;


import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.Predicate;

public class Rule<A, B> implements Function1<A, B>, Predicate<A> {
    private final Predicate<? super A> predicate;
    private final Function1<? super A, ? extends B> callable;

    private Rule(final Predicate<? super A> predicate, final Function1<? super A, ? extends B> callable) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <A, B> Rule<A, B> rule(final Predicate<? super A> predicate, final Function1<? super A, ? extends B> callable) {
        return new Rule<A, B>(predicate, callable);
    }

    @Override
    public boolean matches(final A value) {
        return predicate.matches(value);
    }

    @Override
    public B call(final A input) throws Exception {
        return callable.call(input);
    }
}
