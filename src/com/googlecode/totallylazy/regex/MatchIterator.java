package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public class MatchIterator extends StatefulIterator<MatchResult> {
    private final Matcher matcher;

    public MatchIterator(Matcher matcher) {
        this.matcher = matcher;
        matcher.reset();
    }

    public Option<MatchResult> getNext() {
            if (matcher.find()) {
                return some(matcher.toMatchResult());
            }
        return none();
    }
}
