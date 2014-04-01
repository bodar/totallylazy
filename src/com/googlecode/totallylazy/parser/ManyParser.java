package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.parser.Success.success;

class ManyParser<A> extends Parser<List<A>> {
    private final Parse<? extends A> parser;

    private ManyParser(Parse<? extends A> parser) {
        this.parser = parser;
    }

    static <A> ManyParser<A> many(Parse<? extends A> parser) {
        return new ManyParser<A>(parser);
    }

    public Result<List<A>> parse(Segment<Character> characters) {
        Segment<Character> segment = characters;
        List<A> list = new ArrayList<A>();

        while (!segment.isEmpty()) {
            Result<? extends A> result = parser.parse(segment);
            if (result instanceof Failure) break;
            list.add(result.value());
            segment = result.remainder();
        }
        return success(list, segment);
    }

    @Override
    public String toString() {
        return String.format("many %s", parser);
    }
}
