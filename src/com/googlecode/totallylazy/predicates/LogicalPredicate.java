package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.functions.Function1;

public abstract class LogicalPredicate<T> extends Eq implements Function1<T, Boolean>, Predicate<T> {
    public static <T> LogicalPredicate<T> logicalPredicate(Predicate<? super T> predicate) {
        if(predicate instanceof LogicalPredicate){
            return Unchecked.cast(predicate);
        }
        return new DelegatingPredicate<T>(predicate);
    }

    public static <T> LogicalPredicate<T> logicalPredicate(final Function1<? super T, Boolean> predicate) {
        if(predicate instanceof LogicalPredicate){
            return Unchecked.cast(predicate);
        }
        return new LogicalPredicate<T>() {
            @Override
            public boolean matches(T other) {
                try {
                    return predicate.call(other);
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    @Override
    public LogicalPredicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }

    @Override
    public LogicalPredicate<T> or(Predicate<? super T> predicate){
        return Predicates.<T>or(this, predicate);
    }

    @Override
    public LogicalPredicate<T> not() {
        return Predicates.<T>not(this);
    }

    @Override
    public Boolean call(T t) throws Exception {
        return matches(t);
    }
}
