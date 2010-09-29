package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.iterators.ReadOnlyIterator;

import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class MatchIterator extends ReadOnlyIterator<MatchResult> {
    private final Matcher matcher;
    private MatchResult currentMatch = null;

    public MatchIterator(Matcher matcher) {
        this.matcher = matcher;
        matcher.reset();
    }

    public boolean hasNext() {
        if (currentMatch == null) {
            if (matcher.find()) {
                currentMatch = matcher.toMatchResult();
                return true;
            }
            return false;
        }
        return true;
    }

    public MatchResult next() {
        if (currentMatch != null) {
            final MatchResult result = currentMatch;
            currentMatch = null;
            return result;
        }

        if (hasNext()) {
            return next();
        }

        throw new NoSuchElementException();
    }
}
