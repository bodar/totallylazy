package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class RemainderIs implements Predicate<Integer> {
    private final int divisor;
    private final int remainder;

    public RemainderIs(int divisor, int remainder) {
        this.divisor = divisor;
        this.remainder = remainder;
    }

    public boolean matches(Integer dividend) {
        return dividend % divisor == remainder;
    }
}
