package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.parser.Success.success;

public class CharacterParser extends Parser<Character> {
    private final Predicate<Character> value;

    private CharacterParser(Predicate<Character> value) {
        this.value = value;
    }

    public static CharacterParser character(Predicate<Character> value) {
        return new CharacterParser(value);
    }

    public static CharacterParser character(char value) {
        return character(is(value));
    }

    public static CharacterParser isChar(Predicate<Character> value) {
        return character(value);
    }

    public static CharacterParser isChar(char value) {
        return character(value);
    }

    public static CharacterParser notChar(char value) {
        return character(not(value));
    }

    public static Function1<Character, Parser<Character>> characterParser() {
        return new Function1<Character, Parser<Character>>() {
            @Override
            public Parser<Character> call(Character character) throws Exception {
                return character(character);
            }
        };
    }

    @Override
    public Result<Character> parse(final CharBuffer characters) {
        characters.mark();

        if (!characters.hasRemaining()) {
            characters.reset();
            return fail();
        }

        char character = characters.get();
        if (value.matches(character)) return success(character, characters);

        characters.reset();
        return fail(value, character);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
