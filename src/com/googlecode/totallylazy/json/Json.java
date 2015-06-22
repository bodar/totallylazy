package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.parser.Result;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;

public class Json {
    public static String json(Object value) {
        return JsonWriter.write(value, new StringBuilder()).toString();
    }

    public static <V> Map<String, V> map(String json) {
        return Json.<V>parseMap(json).value();
    }

    public static <V> List<V> list(String json) {
        return Json.<V>parseList(json).value();
    }

    public static Object object(String json) {
        return parseObject(json).value();
    }

    public static <V> Sequence<Pair<String, V>> pairs(Reader json) {
        return Json.<V>parsePairs(json).value();
    }

    public static <V> Sequence<V> sequence(Reader json) {
        return Json.<V>parseSequence(json).value();
    }

    public static <V> Result<Map<String, V>> parseMap(String json) {
        return cast(Grammar.OBJECT.parse(json));
    }

    public static <V> Result<List<V>> parseList(String json) {
        return cast(Grammar.ARRAY.parse(json));
    }

    public static Result<Object> parseObject(String json) {
        return Grammar.VALUE.parse(json);
    }

    public static <V> Result<Sequence<Pair<String, V>>> parsePairs(Reader json) {
        return cast(Grammar.PAIRS.parse(json));
    }

    public static <V> Result<Sequence<V>> parseSequence(Reader json) {
        return cast(Grammar.SEQUENCE.parse(json));
    }

    public static class functions {
        public static Function1<String, Map<String, Object>> toMap = Json::map;

        public static Function1<Object, String> toJson = Json::json;
    }
}
