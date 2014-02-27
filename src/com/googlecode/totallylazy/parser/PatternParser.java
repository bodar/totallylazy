package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.regex.Matches;
import com.googlecode.totallylazy.regex.Regex;

import static com.googlecode.totallylazy.parser.CharacterSequence.charSequence;
import static com.googlecode.totallylazy.parser.Success.success;

class PatternParser extends Parser<String> {
    private final Regex regex;

    private PatternParser(Regex regex) {
        this.regex = regex;
    }

    static PatternParser pattern(Regex regex) {
        return new PatternParser(regex);
    }

    static PatternParser pattern(String value) {
        return pattern(Regex.regex(value));
    }

    @Override
    public String toString() {
        return regex.toString();
    }

    @Override
    public Result<String> parse(Segment<Character> characters) {
        CharacterSequence sequence = charSequence(characters);
        Matches matches = regex.findMatches(sequence);
        if (matches.isEmpty()) return fail(regex, sequence);
        return success(matches.head().group(), sequence.remainder());
    }
}
