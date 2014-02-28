package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Computation.computation;
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
        final Sequence<Result<A>> results = computation(new Callable<Result<A>>() {
                                                            @Override
                                                            public Result<A> call() throws Exception {
                                                                return parser.parse(characters);
                                                            }
                                                        }, new Callable1<Result<A>, Result<A>>() {
                                                            @Override
                                                            public Result<A> call(Result<A> result) throws Exception {
                                                                return parser.parse(result.remainder());
                                                            }
                                                        }
        ).takeWhile(instanceOf(Success.class));

        return new Success<Sequence<A>>() {
            @Override
            public Sequence<A> value() {
                return results.map(new Callable1<Result<A>, A>() {
                    @Override
                    public A call(Result<A> aResult) throws Exception {
                        return aResult.value();
                    }
                });
            }

            @Override
            public Segment<Character> remainder() {
                return results.lastOption().map(new Callable1<Result<A>, Segment<Character>>() {
                    @Override
                    public Segment<Character> call(Result<A> aResult) throws Exception {
                        return aResult.remainder();
                    }
                }).getOrElse(characters);
            }
        };
    }

    @Override
    public String toString() {
        return String.format("sequence %s", parser);
    }
}
