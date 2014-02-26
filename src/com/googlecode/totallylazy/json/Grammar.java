package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.parser.Parser;

import static com.googlecode.totallylazy.Characters.hexDigit;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.string;

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
    //
//    public static final Predicate<Character> UNICODE_CHARACTER = Characters.notAmong("\"\\");
//    public static final Parser<String> STRING = isChar(UNICODE_CHARACTER).source().
//            or(ESCAPED_CHARACTER).many().map(join()).between(isChar('"'), isChar('"'));
//
//    private static Function1<List<String>, String> join() {
//        return new Function1<List<String>, String>() {
//            public String call(List<String> strings) throws Exception {
//                return sequence(strings).toString("");
//            }
//        };
//    }
//
//
//    public static final Parser<Number> NUMBER = Scanners.DECIMAL.map(new Callable1<String, Number>() {
//        public Number call(String value) {
//            return new BigDecimal(value);
//        }
//    });
//
//    private static final Parser.Reference<Object> value = Parser.newReference();
//    public static final Parser<Object> VALUE = value.lazy();
//
//    public static final Parser<Pair<String, Object>> PAIR = Parsers.tuple(STRING, wsChar(':'), VALUE).map(new Callable1<Triple<String, Void, Object>, Pair<String, Object>>() {
//        public Pair<String, Object> call(Triple<String, Void, Object> triple) {
//            return Pair.pair(triple.first(), triple.third());
//        }
//    });
//
//    public static final Parser<Void> SEPARATOR = wsChar(',');
//
//    public static final Parser<List<Object>> ARRAY = Parsers.between(wsChar('['), VALUE.sepBy(SEPARATOR), wsChar(']'));
//
//    public static final Parser<java.util.Map<String, Object>> OBJECT = Parsers.between(wsChar('{'), PAIR.sepBy(SEPARATOR), wsChar('}')).map(new Callable1<List<Pair<String, Object>>, java.util.Map<String, Object>>() {
//        public java.util.Map<String, Object> call(List<Pair<String, Object>> pairs) {
//            return Maps.map(pairs);
//        }
//    });
//
//    static {
//        value.set(ws(Parsers.<Object>or(OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL)));
//    }
}