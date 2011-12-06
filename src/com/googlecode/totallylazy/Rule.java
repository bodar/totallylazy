package com.googlecode.totallylazy;


public class Rule<P, I, O> implements Predicate<P>, Callable1<I, O> {
    private final Predicate<? super P> predicate;
    private final Callable1<? super I, O> callable;

    private Rule(Predicate<? super P> predicate, Callable1<? super I, O> callable) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <P, I, O> Rule<P, I, O> rule(Predicate<? super P> predicate, Callable1<? super I, O> callable) {
        return new Rule<P, I, O>(predicate, callable);
    }

    public boolean matches(P value) {
        return predicate.matches(value);
    }

    public O call(I input) throws Exception {
        return callable.call(input);
    }
}
