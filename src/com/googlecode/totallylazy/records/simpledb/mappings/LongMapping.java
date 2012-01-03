package com.googlecode.totallylazy.records.simpledb.mappings;

import com.googlecode.totallylazy.numbers.Numbers;

import static com.googlecode.totallylazy.numbers.Numbers.parseLexicalString;

public class LongMapping implements Mapping<Long>{
    public Long toValue(String value) {
        return parseLexicalString(value, Long.MIN_VALUE).longValue();
    }

    public String toString(Long value) {
        return Numbers.toLexicalString(value, Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
