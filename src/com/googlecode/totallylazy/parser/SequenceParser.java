package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

public class SequenceParser<A> extends Parser<Sequence<A>> {
    private final Sequence<? extends Parse<? extends A>> parsers;

    private SequenceParser(Iterable<? extends Parse<? extends A>> parsers) {
        this.parsers = sequence(parsers);
    }

    public static <A> SequenceParser<A> sequenceOf(final Iterable<? extends Parse<? extends A>> parsers) {
        return new SequenceParser<A>(parsers);
    }

    public static <A> SequenceParser<A> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b) {
        return new SequenceParser<A>(sequence(a, b));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c) {
        return new SequenceParser<A>(sequence(a, b, c));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d) {
        return new SequenceParser<A>(sequence(a, b, c, d));
    }

    public static <A> SequenceParser<A> sequenceOf(final Parse<? extends A> a, final Parse<? extends A> b, final Parse<? extends A> c, final Parse<? extends A> d, final Parse<? extends A> e) {
        return new SequenceParser<A>(sequence(a, b, c, d, e));
    }

    @SafeVarargs
    public static <A> SequenceParser<A> sequenceOf(final Parse<? extends A>... parsers) {
        return sequenceOf(sequence(parsers));
    }

    @Override
    public Result<Sequence<A>> parse(CharBuffer characters) throws Exception {
        characters.mark();
        List<A> parsed = new ArrayList<A>();
        for (Parse<? extends A> parser : parsers) {
            Result<? extends A> result = parser.parse(characters);
            if (result instanceof Failure) {
                characters.reset();
                return cast(result);
            }
            parsed.add(result.value());
        }
        return success(sequence(parsed), characters);
    }

    @Override
    public String toString() {
        return parsers.toString();
    }

    public String toString(String separator) {
        return parsers.toString(separator);
    }
}
