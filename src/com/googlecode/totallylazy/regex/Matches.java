package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.GroupIterator;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callers.call;

public class Matches extends Sequence<MatchResult> {
    private final Pattern pattern;
    private final CharSequence text;

    public Matches(Pattern pattern, CharSequence text) {
        this.pattern = pattern;
        this.text = text;
    }

    public Iterator<MatchResult> iterator() {
        return new MatchIterator(pattern.matcher(text));
    }

    public String replace(Function1<? super MatchResult, ? extends CharSequence> matched) {
        return replace(returnArgument(CharSequence.class), matched);
    }

    public String replace(Function1<? super CharSequence, ? extends CharSequence> notMatched, Function1<? super MatchResult, ? extends CharSequence> matched) {
        StringBuilder builder = new StringBuilder();
        int position = 0;
        for (MatchResult matchResult : this) {
            CharSequence before = text.subSequence(position, matchResult.start());
            if (before.length() > 0) builder.append(filterNull(call(notMatched, before)));
            builder.append(filterNull(call(matched, (matchResult))));
            position = matchResult.end();
        }
        CharSequence after = text.subSequence(position, text.length());
        if (after.length() > 0) builder.append(filterNull(call(notMatched, after)));
        return builder.toString();
    }

    private static CharSequence filterNull(CharSequence value) {
        return value == null ? "" : value;
    }

    public static class functions {
        public static Function1<Matches, String> replace(final Function1<? super MatchResult, ? extends CharSequence> constant) {
            return new Function1<Matches, String>() {
                @Override
                public String call(Matches matchResults) throws Exception {
                    return matchResults.replace(constant);
                }
            };
        }

        public static Function1<MatchResult, Sequence<String>> groups = new Function1<MatchResult, Sequence<String>>() {
            @Override
            public Sequence<String> call(final MatchResult matchResult) throws Exception {
                return new Sequence<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new GroupIterator(matchResult);
                    }
                };
            }
        };
    }
}
