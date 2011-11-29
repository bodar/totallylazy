package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;

import java.text.ParseException;
import java.util.Date;

import static org.apache.lucene.document.DateTools.dateToString;
import static org.apache.lucene.document.DateTools.stringToDate;

public class DateMapping extends AbstractStringMapping<Date> {
    public DateMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(Date value) {
        return dateToString(value, DateTools.Resolution.MILLISECOND);
    }

    @Override
    protected Date fromString(String value) throws ParseException {
            return stringToDate(value);
    }
}
