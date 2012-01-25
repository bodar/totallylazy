package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.regex.Matches;
import com.googlecode.totallylazy.regex.Regex;

import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.parser.Success.success;

public class PatternParser extends BaseParser<String>{
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
    public Result<String> parse(Sequence<Character> characters) throws Exception {
        Matches matches = regex.findMatches(charSequence(characters));
        if(matches.isEmpty()) {
            return fail();
        }
        MatchResult result = matches.head();
        return success(result.group(), characters.drop(result.end()));
    }

    private CharSequence charSequence(Sequence<Character> characters) {
        return new CharSequenceAdapter(characters);
    }

}
