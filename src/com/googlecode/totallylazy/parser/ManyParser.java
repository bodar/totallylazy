package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Function.returns;

public class ManyParser<A> extends BaseParser<Sequence<A>>{
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
    public Result<Sequence<A>> parse(Sequence<Character> sequence) throws Exception {
        return parser.then(returns(this)).
                map(cons()).
                or(ReturnsParser.returns(Sequences.<A>empty())).
                parse(sequence);
    }

    private Callable1<Pair<A, Sequence<A>>, Sequence<A>> cons() {
        return new Callable1<Pair<A, Sequence<A>>, Sequence<A>>() {
            @Override
            public Sequence<A> call(Pair<A, Sequence<A>> pair) throws Exception {
                return pair.second().cons(pair.first());
            }
        };
    }
}
