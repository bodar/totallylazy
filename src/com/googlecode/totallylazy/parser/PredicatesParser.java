package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.parser.Success.success;

class PredicatesParser implements Parser<String> {
    private final Sequence<? extends Predicate<? super Character>> predicates;

    private PredicatesParser(Sequence<? extends Predicate<? super Character>> predicates) {
        this.predicates = predicates;
    }

    static Parser<String> string(Iterable<? extends Predicate<? super Character>> predicates) {
        return new PredicatesParser(Sequences.sequence(predicates));
    }

    @SafeVarargs
    static Parser<String> string(Predicate<? super Character>... predicates) {
        return new PredicatesParser(Sequences.sequence(predicates));
    }

    @Override
    public Result<String> parse(Segment<Character> characters) {
        Segment<Character> segment = characters;
        StringBuilder result = new StringBuilder();
        for (Predicate<? super Character> predicate : predicates) {
            char a = segment.head();
            result.append(a);
            if (!predicate.matches(a)) return fail(toString(), result.toString());
            segment = segment.tail();
        }
        return success(result.toString(), segment);
    }

    @Override
    public String toString() {
        return predicates.toString("");
    }
}
