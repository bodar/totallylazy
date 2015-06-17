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
        segment.findMatches(path).replace(notMatched -> {
            segments.add(notMatched);
            return null;
        }, match -> {
            String segment1 = match.group(1);
            if(segment1.equals(".")) return null;
            else if(segment1.equals("..")) {
                if(!segments.isEmpty()) segments.removeLast();
            } else {
                segments.add(match.group());
            }
            return null;
        });
        return Sequences.toString(segments, "");
    }
}
