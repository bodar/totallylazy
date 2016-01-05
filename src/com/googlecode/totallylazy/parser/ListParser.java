package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

class ListParser<A> implements Parser<List<A>> {
    private final Sequence<? extends Parser<? extends A>> parsers;

    private ListParser(Sequence<? extends Parser<? extends A>> parsers) {
        this.parsers = parsers;
    }

    static <A> ListParser<A> list(final Iterable<? extends Parser<? extends A>> parsers) {
        return new ListParser<A>(Sequences.sequence(parsers));
    }

    @Override
    public String toString() {
        return parsers.toString();
    }

    @Override
    public Result<List<A>> parse(Segment<Character> characters) {
        Segment<Character> state = characters;
        List<A> parsed = new ArrayList<A>();
        for (Parser<? extends A> parser : parsers) {
            Result<? extends A> result = parser.parse(state);
            if (result instanceof Failure) return cast(result);
            parsed.add(result.value());
            state = result.remainder();
        }
        return success(parsed, state);
    }
}