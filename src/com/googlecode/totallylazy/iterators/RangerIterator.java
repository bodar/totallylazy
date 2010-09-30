package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public class RangerIterator extends ReadOnlyIterator<Integer> {
    private int next;
    private final int end;
    private final int step;

    public RangerIterator(int end) {
        this(0, end);
    }

    public RangerIterator(int start, int end) {
        this(start, end, 1);
    }

    public RangerIterator(int start, int end, int step) {
        this.next = start;
        this.end = end;
        this.step = step;
    }

    public boolean hasNext() {
        return next < end;
    }

    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int result = next;
        next += step;
        return result;
    }
}
