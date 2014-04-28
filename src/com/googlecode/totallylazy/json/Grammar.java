package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.parser.Parse;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.parser.ReferenceParser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.among;
import static com.googlecode.totallylazy.Characters.hexDigit;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.string;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

public interface Grammar {
    static final Parser<Void> NULL = string("null").ignore();

    static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).map(Boolean::valueOf);

    static final Parser<String> ESCAPED_CHARACTER = isChar('\\').next(
            string(Characters.among("\"\\/bfnrt")).or(string(cons(is('u'), repeat(hexDigit).take(4)))).
                    map(Strings::unescape));

    static final Predicate<Character> UNICODE_CHARACTER = Characters.notAmong("\"\\");

    static final Parser<String> STRING = string(is(UNICODE_CHARACTER)).
            or(ESCAPED_CHARACTER).many().map(Parsers.toString).between(isChar('"'), isChar('"'));

    static final Parser<Number> NUMBER = isChar(Characters.digit.or(among(".eE-+"))).many1().map(Parsers.toString).
            map(BigDecimal::new);

    static final Parser<Object> VALUE = Parsers.lazy(new Callable<Parse<Object>>() {
        @Override
        public Parse<Object> call() throws Exception {
            return ws(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL));
        }
    });

    static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).
            map(triple -> Pair.pair(triple.first(), triple.third()));

    static final Parser<?> SEPARATOR = wsChar(',');

    static final Parser<List<Object>> ARRAY = VALUE.sepBy(SEPARATOR).between(wsChar('['), wsChar(']'));

    static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).
            map((Function<List<Pair<String, Object>>, Map<String, Object>>) Maps::map);

    static final Parser<Seq<Pair<String, Object>>> PAIRS = wsChar('{').next(PAIR.sequence());

    static final Parser<Seq<Object>> SEQUENCE = wsChar('[').next(VALUE.seqBy(SEPARATOR));
}