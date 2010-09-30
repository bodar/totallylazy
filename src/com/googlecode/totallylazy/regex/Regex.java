package com.googlecode.totallylazy.regex;

import java.lang.String;import java.util.regex.Pattern;

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

    public Matches matches(String sequence){
        return new Matches(pattern, sequence);
    }
}
