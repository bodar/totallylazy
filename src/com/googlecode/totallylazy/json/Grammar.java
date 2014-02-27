package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Triple;
import com.googlecode.totallylazy.parser.LazyParser;
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
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.string;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

public class Grammar {

    public static final Parser<Void> NULL = string("null").ignore();

    public static final Parser<Boolean> BOOLEAN = string("true").or(string("false")).map(new Callable1<String, Boolean>() {
        public Boolean call(String s) {
            return Boolean.valueOf(s);
        }
    });

    public static final Parser<String> ESCAPED_CHARACTER = isChar('\\').next(
            string(Characters.among("\"\\/bfnrt")).or(string(cons(is('u'), repeat(hexDigit).take(4)))).
                    map(Strings.functions.unescape));

    public static final Predicate<Character> UNICODE_CHARACTER = Characters.notAmong("\"\\");
    private static Function1<Iterable<?>, String> join = new Function1<Iterable<?>, String>() {
        public String call(Iterable<?> strings) throws Exception {
            return sequence(strings).toString("");
        }
    };

    public static final Parser<String> STRING = string(is(UNICODE_CHARACTER)).
            or(ESCAPED_CHARACTER).many().map(join).between(isChar('"'), isChar('"'));

    public static final Parser<Number> NUMBER = isChar(Characters.digit.or(among(".eE-+"))).many1().map(join).map(new Callable1<String, Number>() {
        public Number call(String value) {
            return new BigDecimal(value);
        }
    });

    public static final Parser<Object> VALUE = Parsers.lazy(new Callable<Parse<Object>>() {
        @Override
        public Parse<Object> call() throws Exception {
            return ws(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL));
        }
    });

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(new Callable1<Triple<String, Character, Object>, Pair<String, Object>>() {
        public Pair<String, Object> call(Triple<String, Character, Object> triple) {
            return Pair.pair(triple.first(), triple.third());
        }
    });

    public static final Parser<?> SEPARATOR = wsChar(',');

    public static final Parser<List<Object>> ARRAY = VALUE.sepBy(SEPARATOR).between(wsChar('['), wsChar(']'));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(new Callable1<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
        public java.util.Map<String, Object> call(List<Pair<String, Object>> pairs) {
            return Maps.map(pairs);
        }
    });
}