package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;

public abstract class AbstractPredicate<T> extends com.googlecode.totallylazy.Eq implements Predicate<T>, Function<T, Boolean> {
    @Override
    public Boolean call(T input) throws Exception {
        return matches(input);
    }
}
