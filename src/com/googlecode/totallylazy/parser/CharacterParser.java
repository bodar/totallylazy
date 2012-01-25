package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.Success.success;

public class CharacterParser extends AbstractParser<Character> {
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
        Character head = characters.head();
        if (value.matches(head)) {
            return success(head, characters.tail());
        }
        return fail(head);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
