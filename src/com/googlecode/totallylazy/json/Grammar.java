package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.parser.Parse;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.among;
import static com.googlecode.totallylazy.Characters.hexDigit;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.parser.Parsers.characters;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.string;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

@SuppressWarnings("unchecked")
public class Grammar {
    public static final Parser<Void> NULL = string("null").ignore();

    public static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).map(Boolean::valueOf);

    public static final Parser<String> ESCAPED_CHARACTER = isChar('\\').next(
            string(Characters.among("\"\\/bfnrt")).or(string(cons(is('u'), repeat(hexDigit).take(4)))).
                    map(Strings.functions.unescape));

    public static final Predicate<Character> UNICODE_CHARACTER = Characters.notAmong("\"\\");

    public static final Parser<String> STRING = characters(UNICODE_CHARACTER).
            or(ESCAPED_CHARACTER).many().map(Parsers.toString).between(isChar('"'), isChar('"'));

    public static final Parser<Number> NUMBER = Parsers.characters(Characters.digit.or(among(".eE-+"))).map(BigDecimal::new);

    public static final Parser<Object> VALUE = Parsers.lazy(new Callable<Parse<Object>>() {
        @Override
        public Parse<Object> call() throws Exception {return ws(Parsers.or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL));}
    });

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(triple -> Pair.pair(triple.first(), triple.third()));

    private static final Parser<?> SEPARATOR = wsChar(',');

    public static final Parser<List<Object>> ARRAY = VALUE.sepBy(SEPARATOR).between(wsChar('['), wsChar(']'));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(Maps::map);

    public static final Parser<Sequence<Pair<String, Object>>> PAIRS = wsChar('{').next(PAIR.sequence());

    public static final Parser<Sequence<Object>> SEQUENCE = wsChar('[').next(VALUE.seqBy(SEPARATOR));
}