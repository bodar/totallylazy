package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class MatchIterator extends StatefulIterator<MatchResult> {
    private final Matcher matcher;

    public MatchIterator(Matcher matcher) {
        this.matcher = matcher;
        this.matcher.reset();
    }

    public MatchResult getNext() {
            if (matcher.find()) {
                return matcher.toMatchResult();
            }
        return finished();
    }
}
