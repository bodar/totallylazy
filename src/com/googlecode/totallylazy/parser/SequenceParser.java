package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

public class SequenceParser<A> extends AbstractParser<Sequence<A>> {
    private final Sequence<? extends Parser<? extends A>> parsers;

    private SequenceParser(Sequence<? extends Parser<? extends A>> parsers) {
        this.parsers = parsers;
    }

    public static <A> SequenceParser<A> sequenceOf(final Sequence<? extends Parser<? extends A>> parsers) {
        return new SequenceParser<A>(parsers);
    }

    public static <A> SequenceParser<A> sequenceOf(final Parser<? extends A> a, final Parser<? extends A> b) {
        return new SequenceParser<A>(sequence(a, b));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parser<? extends A> a, final Parser<? extends A> b, final Parser<? extends A> c) {
        return new SequenceParser<A>(sequence(a, b, c));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parser<? extends A> a, final Parser<? extends A> b, final Parser<? extends A> c, final Parser<? extends A> d) {
        return new SequenceParser<A>(sequence(a, b, c, d));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parser<? extends A> a, final Parser<? extends A> b, final Parser<? extends A> c, final Parser<? extends A> d, final Parser<? extends A> e) {
        return new SequenceParser<A>(sequence(a, b, c, d, e));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parser<? extends A>... parsers) {
        return sequenceOf(sequence(parsers));
    }

    @Override
    public String toString() {
        return parsers.toString();
    }

    @Override
    public Result<Sequence<A>> parse(Segment<Character> characters) throws Exception {
        Segment<Character> state = characters;
        List<A> parsed = new ArrayList<A>();
        for (Parser<? extends A> parser : parsers) {
            Result<? extends A> result = parser.parse(state);
            if (result instanceof Failure) return cast(result);
            Success<A> success = cast(result);
            parsed.add(success.value());
            state = success.remainder();
        }
        return success(sequence(parsed), state);
    }
}