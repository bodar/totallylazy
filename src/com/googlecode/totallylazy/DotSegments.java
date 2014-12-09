package com.googlecode.totallylazy;

import com.googlecode.totallylazy.regex.Regex;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.regex.Regex.regex;

public class DotSegments {
    private static final Regex segment = regex("/?([^/]+)");

    public static String remove(String path) {
        final Deque<CharSequence> segments = new ArrayDeque<CharSequence>();
        segment.findMatches(path).replace(new Function1<CharSequence, CharSequence>() {
            @Override
            public CharSequence call(CharSequence notMatched) throws Exception {
                segments.add(notMatched);
                return null;
            }
        }, new Function1<MatchResult, CharSequence>() {
            @Override
            public CharSequence call(MatchResult match) throws Exception {
                String segment = match.group(1);
                if(segment.equals(".")) return null;
                else if(segment.equals("..")) {
                    if(!segments.isEmpty()) segments.removeLast();
                } else {
                    segments.add(match.group());
                }
                return null;
            }
        });
        return Sequences.toString(segments, "");
    }
}
