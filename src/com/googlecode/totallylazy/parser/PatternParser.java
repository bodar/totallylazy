package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.regex.Matches;
import com.googlecode.totallylazy.regex.Regex;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.parser.CharacterSequence.charSequence;
import static com.googlecode.totallylazy.parser.Success.success;

public class PatternParser extends Parser<String> {
    private final Regex regex;

    private PatternParser(Regex regex) {
        this.regex = regex;
    }

    public static PatternParser pattern(Regex regex) {
        return new PatternParser(regex);
    }

    public static PatternParser pattern(String value) {
        return pattern(Regex.regex(value));
    }

    @Override
    public String toString() {
        return regex.toString();
    }

    @Override
    public Result<String> parse(CharBuffer characters) throws Exception {
        characters.mark();
        Matches matches = regex.findMatches(characters);
        if (matches.isEmpty()) {
            characters.reset();
            return fail(regex, characters);
        }
        String group = matches.head().group();
        characters.position(group.length());
        return success(group, characters);
    }
}
