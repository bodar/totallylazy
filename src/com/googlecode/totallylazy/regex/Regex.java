package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Sequence;

import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Regex {
    private final Pattern pattern;

    private Regex(Pattern pattern) {
        this.pattern = pattern;
    }

    public static Regex regex(String pattern){
        return new Regex(Pattern.compile(pattern));
    }

    public static Regex regex(String pattern, int flags){
        return new Regex(Pattern.compile(pattern, flags));
    }

    public static Regex regex(Pattern pattern){
        return new Regex(pattern);
    }

    public Matches matches(CharSequence sequence){
        return new Matches(pattern, sequence);
    }

    public Sequence<String> split(CharSequence value) {
        return sequence(pattern.split(value));
    }
}
