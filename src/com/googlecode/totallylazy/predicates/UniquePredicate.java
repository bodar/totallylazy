package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;

import java.util.HashSet;
import java.util.Set;

public class UniquePredicate<T, S> extends LogicalPredicate<T> {
    private final Callable1<? super T, ? extends S> callable;
    private final Set<S> valuesSeen = new HashSet<S>();

    public UniquePredicate(Callable1<? super T, ? extends S> callable) {
        this.callable = callable;
    }

    @Override
    public boolean matches(T other) {
        return valuesSeen.add(Callers.call(callable, other));
    }
}
