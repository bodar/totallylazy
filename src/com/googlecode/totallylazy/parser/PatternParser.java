package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.regex.Regex;

import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.parser.CharacterSequence.charSequence;
import static com.googlecode.totallylazy.parser.Success.success;

class PatternParser implements Parser<String> {
    private final Pattern pattern;

    private PatternParser(Pattern pattern) {
        this.pattern = pattern;
    }

    static PatternParser pattern(Pattern pattern) {
        return new PatternParser(pattern);
    }

    static PatternParser pattern(String pattern) {
        return pattern(Pattern.compile(pattern));
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

    @Override
    public Result<String> parse(Segment<Character> characters) {
        CharacterSequence sequence = charSequence(characters);
        Matcher matcher = pattern.matcher(sequence);
        if (matches(matcher)) return success(matcher.group(), drop(matcher.end(), characters));
        return fail(toString(), sequence);
    }

    private boolean matches(Matcher matcher) {
        try {
            return matcher.lookingAt();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static <T> Segment<T> drop(int count, Segment<T> segment) {
        Segment<T> current = segment;
        for (int i = 0; i < count; i++) {
            if(current.isEmpty()) return current;
             current = current.tail();
        }
        return current;
    }
}
