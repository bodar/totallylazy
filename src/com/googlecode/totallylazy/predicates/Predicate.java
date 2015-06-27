package com.googlecode.totallylazy.predicates;

public interface Predicate<T> {
    boolean matches(T other);

    default Predicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }

    default Predicate<T> or(Predicate<? super T> predicate){
        return Predicates.<T>or(this, predicate);
    }

    default Predicate<T> not() {
        return Predicates.<T>not(this);
    }

}
