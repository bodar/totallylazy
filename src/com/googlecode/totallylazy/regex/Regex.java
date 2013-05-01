package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Regex extends Mapper<CharSequence, Matches> implements Predicate<CharSequence>{
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

    public Matches findMatches(CharSequence sequence){
        return new Matches(pattern, sequence);
    }

    public Sequence<String> split(CharSequence value) {
        return sequence(pattern.split(value));
    }

    @Override
    public boolean matches(CharSequence other) {
        return pattern.matcher(other).matches();
    }

    @Override
    public String toString() {
        return pattern.pattern();
    }

    public MatchResult match(CharSequence value) {
        return findMatches(value).head();
    }

    @Override
    public Matches call(CharSequence charSequence) throws Exception {
        return findMatches(charSequence);
    }
}
