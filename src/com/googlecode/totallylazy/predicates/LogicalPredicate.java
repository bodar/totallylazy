package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unchecked;

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
    public Boolean call(T t) throws Exception {
        return matches(t);
    }
}
