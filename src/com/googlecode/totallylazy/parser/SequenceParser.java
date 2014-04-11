package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Sequences.forwardOnly;

class SequenceParser<A> extends Parser<Seq<A>> {
    private final Parse<A> parser;

    private SequenceParser(Parse<A> parser) {
        this.parser = parser;
    }

    static <A> SequenceParser<A> sequence(Parse<A> parser) {
        return new SequenceParser<A>(Unchecked.<Parse<A>>cast(parser));
    }

    public Result<Seq<A>> parse(final Segment<Character> characters) {
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

        return new Success<Seq<A>>() {
            @Override
            public Seq<A> value() {
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
