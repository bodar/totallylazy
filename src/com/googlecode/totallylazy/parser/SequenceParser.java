package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Sequences.forwardOnly;

class SequenceParser<A> implements Parser<Sequence<A>> {
    private final Parser<A> parser;

    private SequenceParser(Parser<A> parser) {
        this.parser = parser;
    }

    static <A> SequenceParser<A> sequence(Parser<A> parser) {
        return new SequenceParser<A>(Unchecked.<Parser<A>>cast(parser));
    }

    public Result<Sequence<A>> parse(final Segment<Character> characters) {
        final AtomicReference<Segment<Character>> remainder = new AtomicReference<Segment<Character>>(characters);

        final StatefulIterator<Result<A>> iterator = new StatefulIterator<Result<A>>() {
            @Override
            protected Result<A> getNext() throws Exception {
                Result<A> result = parser.parse(remainder.get());
                if (result.failure()) return finished();
                remainder.set(result.remainder());
                return result;
            }
        };

        return new Success<Sequence<A>>() {
            @Override
            public Sequence<A> value() {
                return forwardOnly(iterator).map(Callables.<A>value());
            }

            @Override
            public Segment<Character> remainder() {
                return remainder.get();
            }
        };
    }

    @Override
    public String toString() {
        return String.format("sequence %s", parser);
    }
}
