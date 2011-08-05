package com.googlecode.totallylazy.records.simpledb.mappings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapping implements Mapping<Date>{
    private final DateFormat dateFormat;

    public DateMapping(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateMapping() {
        this(new SimpleDateFormat());
    }

    public Date toValue(String value) throws ParseException {
        return dateFormat.parse(value);
    }

    public String toString(Date value) {
        return dateFormat.format(value);
    }
}
