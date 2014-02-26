package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.parser.Success.success;

public class StringParser extends Parser<String> {
    private final Sequence<? extends Predicate<? super Character>> predicates;

    private StringParser(Sequence<? extends Predicate<? super Character>> predicates) {
        this.predicates = predicates;
    }

    public static Parser<String> string(Iterable<? extends Predicate<? super Character>> predicates) {
        return new StringParser(sequence(predicates));
    }

    public static Parser<String> string(Predicate<? super Character>... predicates) {
        return new StringParser(sequence(predicates));
    }

    public static Parser<String> string(String predicates) {
        return new StringParser(characters(predicates).map(new Function1<Character, Predicate<Character>>() {
            @Override
            public Predicate<Character> call(Character character) throws Exception {
                return Predicates.is(character);
            }
        }));
    }

    @Override
    public Result<String> parse(Segment<Character> input) {
        Segment<Character> segment = input;
        StringBuilder result = new StringBuilder();
        for (Predicate<? super Character> predicate : predicates) {
            char a = segment.head();
            if (!predicate.matches(a)) return fail(toString(), a);
            segment = segment.tail();
            result.append(a);
        }
        return success(result.toString(), segment);
    }

    @Override
    public String toString() {
        return predicates.toString("");
    }
}
