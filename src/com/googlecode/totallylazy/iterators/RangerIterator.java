package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.numbers.Numbers;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.numbers.Numbers.lessThan;

public class RangerIterator extends ReadOnlyIterator<Number> {
    private Number next;
    private final Number end;
    private final Number step;

    public   RangerIterator(Number end) {
        this(0, end);
    }

    public RangerIterator(Number start, Number end) {
        this(start, end, 1);
    }

    public RangerIterator(Number start, Number end, Number step) {
        this.next = start;
        this.end = end;
        this.step = step;
    }

    public boolean hasNext() {
        return lessThan(next,end);
    }

    public Number next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Number result = next;
        next = Numbers.add(next, step);
        return result;
    }
}
