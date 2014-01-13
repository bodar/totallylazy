package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unchecked;

public abstract class LogicalPredicate<T> extends Function1<T, Boolean> implements Predicate<T> {
    public static <T> LogicalPredicate<T> logicalPredicate(Predicate<? super T> predicate) {
        if(predicate instanceof LogicalPredicate){
            return Unchecked.cast(predicate);
        }
        return new DelegatingPredicate<T>(predicate);
    }

    public static <T> LogicalPredicate<T> logicalPredicate(final Function<? super T, Boolean> predicate) {
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

    public LogicalPredicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }

    public LogicalPredicate<T> or(Predicate<? super T> predicate){
        return Predicates.<T>or(this, predicate);
    }

    public LogicalPredicate<T> not() {
        return Predicates.<T>not(this);
    }

    @Override
    public Boolean call(T t) throws Exception {
        return matches(t);
    }
}
