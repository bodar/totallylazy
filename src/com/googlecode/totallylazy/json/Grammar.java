package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.among;
import static com.googlecode.totallylazy.Characters.hexDigit;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.parser.Parsers.characters;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.string;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

@SuppressWarnings("unchecked")
public interface Grammar {
    Parser<Void> NULL = string("null").ignore();

    Parser<Boolean> BOOLEAN = string("true").or(string("false")).map(Boolean::valueOf);

    Parser<String> ESCAPED_CHARACTER = isChar('\\').next(
            string(Characters.among("\"\\/bfnrt")).or(string(cons(is('u'), repeat(hexDigit).take(4)))).
                    map(Strings.functions.unescape));

    Predicate<Character> UNICODE_CHARACTER = Characters.notAmong("\"\\");

    Parser<String> STRING = characters(UNICODE_CHARACTER).
            or(ESCAPED_CHARACTER).many().map(Parsers.toString).between(isChar('"'), isChar('"'));

    Parser<Number> NUMBER = Parsers.characters(Characters.digit.or(among(".eE-+"))).map(chars -> new BigDecimal(chars.toString()));

    Parser<Object> VALUE = Parsers.lazy(new Callable<Parser<Object>>() {
        @Override
        public Parser<Object> call() throws Exception {return ws(Parsers.or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL));}
    });

    Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(triple -> Pair.pair(triple.first(), triple.third()));

    Parser<?> SEPARATOR = wsChar(',');

    Parser<List<Object>> ARRAY = VALUE.sepBy(SEPARATOR).between(wsChar('['), wsChar(']'));

    Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(Maps::map);

    Parser<Sequence<Pair<String, Object>>> PAIRS = wsChar('{').next(PAIR.sequence());

    Parser<Sequence<Object>> SEQUENCE = wsChar('[').next(VALUE.seqBy(SEPARATOR));
}