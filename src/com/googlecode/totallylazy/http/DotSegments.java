package com.googlecode.totallylazy.http;

import com.googlecode.totallylazy.Sequences;
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
            switch (match.group(1)) {
                case ".":
                    return null;
                case "..":
                    if (!segments.isEmpty()) segments.removeLast();
                    break;
                default:
                    segments.add(match.group());
                    break;
            }
            return null;
        });
        return Sequences.toString(segments, "");
    }
}
