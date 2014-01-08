package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;
import java.util.regex.MatchResult;

public final class GroupIterator extends ReadOnlyIterator<String> {
    private final MatchResult result;
    private int index = 1;

    public GroupIterator(MatchResult result) {
        this.result = result;
    }

    public final boolean hasNext() {
        return index <= result.groupCount();
    }

    public final String next() {
        if(hasNext()){
            return result.group(index++);
        }
        throw new NoSuchElementException();
    }
}
