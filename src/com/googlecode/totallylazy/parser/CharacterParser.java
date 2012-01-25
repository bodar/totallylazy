package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.Success.success;

public class CharacterParser extends BaseParser<Character> {
    private final Predicate<Character> value;

    private CharacterParser(Predicate<Character> value) {
        this.value = value;
    }

    public static CharacterParser character(char value) {
        return character(is(value));
    }

    public static CharacterParser character(Predicate<Character> value) {
        return new CharacterParser(value);
    }

    @Override
    public Result<Character> parse(final Sequence<Character> characters) {
        return characters.headOption().
                map(ifMatches(characters)).getOrElse(fail());
                }

    private Callable1<Character, Result<Character>> ifMatches(final Sequence<Character> characters) {
        return new Callable1<Character, Result<Character>>() {
            @Override
            public Result<Character> call(Character character) throws Exception {
                return value.matches(character) ? success(character, characters.tail()) : fail();
                }
            };
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
