package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.numbers.Numbers;
import org.apache.lucene.document.Field;

import java.text.ParseException;

public class LexicalIntegerMapping extends AbstractStringMapping<Integer> {
    public LexicalIntegerMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(Integer value) {
        return Numbers.toLexicalString(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected Integer fromString(String value) throws ParseException {
            return Numbers.parseLexicalString(value, Integer.MIN_VALUE).intValue();
    }
}
