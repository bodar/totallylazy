package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.LazyException;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

import java.text.ParseException;
import java.util.Date;

import static org.apache.lucene.document.DateTools.dateToString;
import static org.apache.lucene.document.DateTools.stringToDate;

public class DateMapping extends StringMapping<Date> {
    @Override
    protected String toString(Date value) {
        return dateToString(value, DateTools.Resolution.MILLISECOND);
    }

    @Override
    protected Date fromString(String value) throws ParseException {
            return stringToDate(value);
    }
}
