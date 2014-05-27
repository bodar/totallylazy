package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.parser.Result;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Json {
    static String json(Object value) { return JsonWriter.write(value, new StringBuilder()).toString(); }

    static <V> Map<String, V> map(String json) { return Json.<V>parseMap(json).value(); }

    static <V> List<V> list(String json) { return Json.<V>parseList(json).value(); }

    static Object object(String json) { return parseObject(json).value(); }

    static <V> Sequence<Pair<String, V>> pairs(Reader json) { return Json.<V>parsePairs(json).value(); }

    static <V> Sequence<V> sequence(Reader json) { return Json.<V>parseSequence(json).value(); }

    static <V> Result<Map<String, V>> parseMap(String json) { return cast(Grammar.OBJECT.parse(json)); }

    static <V> Result<List<V>> parseList(String json) { return cast(Grammar.ARRAY.parse(json)); }

    static Result<Object> parseObject(String json) { return Grammar.VALUE.parse(json); }

    static <V> Result<Sequence<Pair<String, V>>> parsePairs(Reader json) { return cast(Grammar.PAIRS.parse(json)); }

    static <V> Result<Sequence<V>> parseSequence(Reader json) {
        return cast(Grammar.SEQUENCE.parse(json));
    }
}
