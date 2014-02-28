package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Triple;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.parser.ReferenceParser;

import java.math.BigDecimal;
import java.util.List;

import static com.googlecode.totallylazy.Characters.among;
import static com.googlecode.totallylazy.Characters.hexDigit;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.repeat;
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

    public static final Parser<String> STRING = string(is(UNICODE_CHARACTER)).
            or(ESCAPED_CHARACTER).many().map(Parsers.toString).between(isChar('"'), isChar('"'));

    public static final Parser<Number> NUMBER = isChar(Characters.digit.or(among(".eE-+"))).many1().map(Parsers.toString).map(new Callable1<String, Number>() {
        public Number call(String value) {
            return new BigDecimal(value);
        }
    });

    private static final ReferenceParser<Object> ref = Parsers.reference();
    public static final Parser<Object> VALUE = ref;

    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(new Callable1<Triple<String, Character, Object>, Pair<String, Object>>() {
        public Pair<String, Object> call(Triple<String, Character, Object> triple) {
            return Pair.pair(triple.first(), triple.third());
        }
    });

    private static final Parser<?> SEPARATOR = wsChar(',');

    public static final Parser<List<Object>> ARRAY = VALUE.sepBy(SEPARATOR).between(wsChar('['), wsChar(']'));

    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(new Callable1<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
        public java.util.Map<String, Object> call(List<Pair<String, Object>> pairs) {
            return Maps.map(pairs);
        }
    });

    static {
        ref.set(ws(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL)));
    }

    public static final Parser<Sequence<Pair<String, Object>>> PAIRS = wsChar('{').next(PAIR.sequence());

    public static final Parser<Sequence<Object>> SEQUENCE = wsChar('[').next(VALUE.seqBy(SEPARATOR));
}