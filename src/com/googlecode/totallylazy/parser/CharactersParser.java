package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.parser.Success.success;

class CharactersParser implements Parser<CharSequence> {
    private final Predicate<Character> predicate;

    private CharactersParser(Predicate<Character> predicate) {
        this.predicate = predicate;
    }

    public static CharactersParser characters(Predicate<Character> predicate) {return new CharactersParser(predicate);}

    @Override
    public String toString() {
        return String.format("many %s", predicate);
    }


    @Override
    public Result<CharSequence> parse(Segment<Character> characters) {
        if(characters.isEmpty()) return fail(predicate, "");
        Segment<Character> segment = characters;
        StringBuilder result = new StringBuilder();
        while (!segment.isEmpty()) {
            Character head = segment.head();
            if (!predicate.matches(head)) break;
            result.append(head);
            segment = segment.tail();
        }
        if(result.length() == 0) return fail(predicate, characters.head());
        return success(result, segment);
    }
}
