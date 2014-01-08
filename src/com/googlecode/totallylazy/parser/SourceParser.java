package com.googlecode.totallylazy.parser;

import java.nio.CharBuffer;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Success.success;

public class SourceParser extends Parser<String> {
    private final Parse<?> parser;

    private SourceParser(Parse<?> parser) {
        this.parser = parser;
    }

    public static SourceParser source(Parse<?> parser) {return new SourceParser(parser);}

    @Override
    public java.lang.String toString() {
        return parser.toString();
    }

    @Override
    public Result<String> parse(CharBuffer sequence) throws Exception {
        int start = sequence.position();
        Result<?> result = parser.parse(sequence);
        if(result instanceof Success){
            int end = sequence.position();
            sequence.position(start);
            return success(sequence.subSequence(0, end - start).toString(), (CharBuffer) sequence.position(end));
        }
        return cast(result);
    }
}
