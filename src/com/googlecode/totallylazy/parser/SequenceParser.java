package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.ForwardOnlySequence;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.yatspec.state.Results;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Computation.computation;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Predicates.instanceOf;

class SequenceParser<A> extends Parser<Sequence<A>> {
    private final Parse<A> parser;

    private SequenceParser(Parse<A> parser) {
        this.parser = parser;
    }

    static <A> SequenceParser<A> sequence(Parse<A> parser) {
        return new SequenceParser<A>(Unchecked.<Parse<A>>cast(parser));
    }

    public Result<Sequence<A>> parse(final Segment<Character> characters) {
        final AtomicReference<Segment<Character>> remainder = new AtomicReference<Segment<Character>>(characters);

        final StatefulIterator<Result<A>> iterator = new StatefulIterator<Result<A>>() {
            @Override
            protected Result<A> getNext() throws Exception {
                Result<A> result = parser.parse(remainder.get());
                if(result.failure()) return finished();
                remainder.set(result.remainder());
                return result;
            }
        };

        final Sequence<Result<A>> results = Sequences.forwardOnly(iterator);

        return new Success<Sequence<A>>() {
            @Override
            public Sequence<A> value() {
                return results.map(Callables.<A>value());
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
