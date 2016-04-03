package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Extractor;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Regex implements Predicate<CharSequence>, Function1<CharSequence, Matches>, Extractor<CharSequence, String>{
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
        return Sequences.sequence(pattern.split(value));
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
    public Sequence<String> extract(CharSequence value){
        return Sequences.flatten(findMatches(value).headOption().map(Matches.functions.groups));
    }

    @Override
    public Matches call(CharSequence charSequence) throws Exception {
        return findMatches(charSequence);
    }

    public Sequence<Result> sequence(CharSequence text) {
        return new Sequence<Result>() {
            @Override
            public Iterator<Result> iterator() {
                return Iterators.flatten(new ResultIterator(pattern.matcher(text), text));
            }
        };
    }
}
