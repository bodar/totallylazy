package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class CountTo<T> implements Predicate<T> {
    private int count;

    public CountTo(int count) {
        this.count = count;
    }

    public boolean matches(T other) {
        if(count > 0){
            count--;
            return true;
        }
        return false;
    }
}
