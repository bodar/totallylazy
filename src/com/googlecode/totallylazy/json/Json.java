package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;

public class Json {
    public static String json(Object value) {
        return JsonWriter.write(value, new StringBuilder()).toString();
    }

    public static <V> Map<String, V> map(String json) {
        return cast(Grammar.OBJECT.parse(json).value());
    }

    public static <V> List<V> list(String json) {
        return cast(Grammar.ARRAY.parse(json).value());
    }

    public static Object object(String json) {
        return Grammar.VALUE.parse(json).value();
    }

    public static <V> Sequence<Pair<String, V>> pairs(Reader json) {
        return cast(Grammar.PAIRS.parse(json).value());
    }

    public static <V> Sequence<V> sequence(Reader json) {
        return cast(Grammar.SEQUENCE.parse(json).value());
    }

    public static class functions {
        public static Mapper<String, Map<String, Object>> toMap = new Mapper<String, Map<String, Object>>() {
            public Map<String, Object> call(String json) throws Exception {
                return Json.map(json);
            }
        };

        public static Mapper<Object, String> toJson = new Mapper<Object, String>() {
            public String call(Object value) throws Exception {
                return Json.json(value);
            }
        };
    }
}
