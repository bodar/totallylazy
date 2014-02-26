package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.parser.Success.success;

public class ManyParser<A> extends Parser<List<A>> {
    private final Parse<? extends A> parser;

    private ManyParser(Parse<? extends A> parser) {
        this.parser = parser;
    }

    public static <A> ManyParser<A> many(Parse<? extends A> parser) {
        return new ManyParser<A>(parser);
    }

    @Override
    public String toString() {
        return String.format("many %s", parser);
    }

    public Result<List<A>> parse(Segment<Character> sequence) {
        Segment<Character> segment = sequence;
        List<A> list = new ArrayList<A>();

        while (!segment.isEmpty()) {
            Result<? extends A> result = parser.parse(segment);
            if (result instanceof Failure) break;
            list.add(result.value());
            segment = result.remainder();
        }
        return success(list, segment);
    }
}
