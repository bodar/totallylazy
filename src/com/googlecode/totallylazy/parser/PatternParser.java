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

class PatternParser extends Parser<String> {
    private final Pattern pattern;
    private final String pretty;

    private PatternParser(Pattern pattern, String pretty) {
        this.pattern = pattern;
        this.pretty = pretty;
    }

    static PatternParser pattern(Pattern pattern, String pretty) {
        return new PatternParser(pattern, pretty);
    }

    static PatternParser pattern(String pattern, String pretty) {
        return pattern(Pattern.compile(pattern), pretty);
    }

    @Override
    public String toString() {
        return Strings.isEmpty(pretty) ? pattern.toString() : pretty;
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
