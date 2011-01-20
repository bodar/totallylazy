package com.googlecode.totallylazy.predicates;


public class CountTo<T> extends LogicalPredicate<T> {
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
