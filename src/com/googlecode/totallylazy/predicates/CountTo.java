package com.googlecode.totallylazy.predicates;


import static com.googlecode.totallylazy.numbers.Numbers.decrement;
import static com.googlecode.totallylazy.numbers.Numbers.isPositive;

public class CountTo extends LogicalPredicate<Object> {
    private Number count;

    public CountTo(Number count) {
        this.count = count;
    }

    public boolean matches(Object other) {
        if(isPositive(count)){
            count = decrement(count);
            return true;
        }
        return false;
    }
}
