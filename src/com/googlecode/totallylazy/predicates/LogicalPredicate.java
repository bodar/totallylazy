package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unchecked;

public abstract class LogicalPredicate<T> implements Predicate<T> {
    public static <T> LogicalPredicate<T> logicalPredicate(Predicate<? super T> predicate) {
        if(predicate instanceof LogicalPredicate){
            return Unchecked.cast(predicate);
        }
        return new DelegatingPredicate<T>(predicate);
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

    public Function1<T, Boolean> asCallable() {
        return new Function1<T, Boolean>() {
            @Override
            public Boolean call(T instance) throws Exception {
                return matches(instance);
            }
        };
    }

}
