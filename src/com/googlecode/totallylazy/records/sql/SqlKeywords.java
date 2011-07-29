package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Keywords;

public class SqlKeywords {
    public static Keyword<Object> keyword(String value) {
        return Keywords.keyword(value, extractName(value));
    }

    public static <T> Keyword<T> keyword(String value, Class<T> aClass) {
        return Keywords.keyword(value, extractName(value), aClass);
    }

    private static String extractName(String value) {
        String[] parts = value.split("\\.");
        return parts[parts.length - 1];
    }

}
