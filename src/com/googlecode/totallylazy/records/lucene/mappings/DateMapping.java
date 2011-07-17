package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.LazyException;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import java.text.ParseException;
import java.util.Date;

import static org.apache.lucene.document.DateTools.dateToString;
import static org.apache.lucene.document.DateTools.stringToDate;

public class DateMapping implements Mapping<Date> {
    public Fieldable toField(String name, Date value) {
        return new Field(name, dateToString(((Date) value), DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED);
    }

    public Date toValue(Fieldable fieldable) {
        try {
            return stringToDate(fieldable.stringValue());
        } catch (ParseException e) {
            throw new LazyException(e);
        }
    }

    public Query equalTo(String name, Date value) {
        return new TermQuery(new Term(name, dateToString(value, DateTools.Resolution.MILLISECOND)));
    }
}
