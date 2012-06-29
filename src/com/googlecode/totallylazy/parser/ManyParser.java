package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Function.returns;

public class ManyParser<A> extends BaseParser<Segment<A>>{
    private final BaseParser<A> parser;

    private ManyParser(Parser<? extends A> parser) {
        this.parser = parser(parser);
    }

    public static <A> ManyParser<A> many(Parser<? extends A> parser) {
        return new ManyParser<A>(parser);
    }

    @Override
    public String toString() {
        return String.format("many %s", parser);
    }

    @Override
    public Result<Segment<A>> parse(Segment<Character> sequence) throws Exception {
        return parser.then(returns(this)).
                map(cons()).
                or(ReturnsParser.returns(Segment.constructors.<A>emptySegment())).
                parse(sequence);
    }

    private Callable1<Pair<A, Segment<A>>, Segment<A>> cons() {
        return new Callable1<Pair<A, Segment<A>>, Segment<A>>() {
            @Override
            public Segment<A> call(Pair<A, Segment<A>> pair) throws Exception {
                return pair.second().cons(pair.first());
            }
        };
    }
}
