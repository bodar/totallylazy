package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.numbers.Numbers;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.numbers.Numbers.lessThan;

public final class RangerIterator extends ReadOnlyIterator<Number> {
    private Number next;
    private final Number end;
    private final Number step;

    public RangerIterator(final Number start, final Number end) {
        this(start, end, 1);
    }

    public RangerIterator(final Number start, final Number end, final Number step) {
        this.next = start;
        this.end = end;
        this.step = step;
    }

    public final boolean hasNext() {
        return lessThan(next,end);
    }

    public final Number next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Number result = next;
        next = Numbers.add(next, step);
        return result;
    }
}
