package com.googlecode.totallylazy.records.simpledb.mappings;

import com.googlecode.totallylazy.time.DateConverter;
import com.googlecode.totallylazy.time.DateFormatConverter;

import java.text.ParseException;
import java.util.Date;

public class DateMapping implements Mapping<Date>{
    private final DateConverter converter;

    public DateMapping(DateConverter converter) {
        this.converter = converter;
    }

    public DateMapping() {
        this(DateFormatConverter.defaultConverter());
    }

    public Date toValue(String value) throws ParseException {
        return converter.parse(value);
    }

    public String toString(Date value) {
        return converter.format(value);
    }
}
