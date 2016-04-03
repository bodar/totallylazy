package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static com.googlecode.totallylazy.Callers.call;

public class ResultIterator extends StatefulIterator<Iterator<Result>> {
    private final Matcher matcher;
    private final CharSequence text;
    private int position = 0;

    public ResultIterator(Matcher matcher, CharSequence text) {
        this.matcher = matcher;
        this.text = text;
    }

    @Override
    protected Iterator<Result> getNext() throws Exception {
        List<Result> results = new ArrayList<>();
        if (position == text.length()) return finished();
        if (matcher.find()) {
            unmatched(results, matcher.start());
            results.add(Result.matched(matcher.group()));
            position = matcher.end();
        } else {
            unmatched(results, text.length());
            position = text.length();
        }
        return results.iterator();
    }

    private void unmatched(List<Result> results, int end) {
        CharSequence unmatched = text.subSequence(position, end);
        if (unmatched.length() > 0) results.add(Result.unmatched(unmatched));
    }
}
