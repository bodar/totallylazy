package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.numbers.Numbers;
import org.apache.lucene.document.Field;

import java.text.ParseException;

public class LexicalLongMapping extends AbstractStringMapping<Long> {
    public LexicalLongMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(Long value) {
        return Numbers.toLexicalString(value, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    protected Long fromString(String value) throws ParseException {
            return Numbers.parseLexicalString(value, Long.MIN_VALUE).longValue();
    }
}
