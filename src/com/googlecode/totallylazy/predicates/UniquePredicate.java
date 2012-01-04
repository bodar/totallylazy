package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;

import java.util.HashSet;
import java.util.Set;

import static com.googlecode.totallylazy.Callers.call;

public class UniquePredicate<T, S> extends LogicalPredicate<T> {
    private final Callable1<? super T, S> callable;
    private final Set<S> valuesSeen = new HashSet<S>();

    public UniquePredicate(Callable1<? super T, S> callable) {
        this.callable = callable;
    }

    @Override
    public boolean matches(T other) {
        return valuesSeen.add(call(callable, other));
    }
}
