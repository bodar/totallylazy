package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Function.returns;

public class ManyParser<A> extends BaseParser<Segment<A>> {
    private final BaseParser<A> parser;
    private final Function1<Result<A>, Segment<Character>> remainder = Success.functions.<A>remainder();
    private final Function1<Segment<Character>, Result<A>> parse;

    private ManyParser(Parser<? extends A> parser) {
        this.parser = parser(parser);
        parse = functions.parse(this.parser);
    }

    public static <A> ManyParser<A> many(Parser<? extends A> parser) {
        return new ManyParser<A>(parser);
    }

    @Override
    public String toString() {
        return String.format("many %s", parser);
    }

// lazy version
//    @Override
//    public Result<Segment<A>> parse(final Segment<Character> sequence) throws Exception {
//        final Sequence<Result<A>> result = computation(parse.deferApply(sequence), remainder.then(parse)).takeWhile(instanceOf(Success.class));
//        return Success.<Segment<A>>success(returns(result.map(Callables.<A>value())), remainderOf(result));
//    }
//
//    private Callable<Segment<Character>> remainderOf(final Sequence<Result<A>> computation) {
//        return new Callable<Segment<Character>>() {
//            @Override
//            public Segment<Character> call() throws Exception {
//                return computation.map(remainder).last();
//            }
//        };
//    }

    //Eager version
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
