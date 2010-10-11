package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.numbers.Numbers;

import static com.googlecode.totallylazy.numbers.Numbers.remainder;

public class RemainderIs implements Predicate<Number> {
    private final Number divisor;
    private final Number remainder;

    public RemainderIs(Number divisor, Number remainder) {
        this.divisor = divisor;
        this.remainder = remainder;
    }

    public boolean matches(Number dividend) {
        return Numbers.equiv(remainder(dividend, divisor), remainder);
    }

}
