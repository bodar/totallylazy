package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.parser.Success.success;

class CharacterParser extends Parser<Character> {
    private final Predicate<Character> predicate;

    private CharacterParser(Predicate<Character> predicate) {
        this.predicate = predicate;
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
        return character -> character(character);
    }

    @Override
    public Result<Character> parse(final Segment<Character> characters) {
        if(characters.isEmpty()) return fail(predicate, "[EOF]");
        Character c = characters.head();
        return predicate.matches(c) ?
                success(c, characters.tail()) :
                fail(predicate, c);
    }

    @Override
    public String toString() {
        return predicate.toString();
    }
}
