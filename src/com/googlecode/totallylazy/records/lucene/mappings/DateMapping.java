package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.time.DateFormatConverter;
import com.googlecode.totallylazy.time.Dates;
import org.apache.lucene.document.Field;

import java.text.ParseException;
import java.util.Date;

import static com.googlecode.totallylazy.Sequences.sequence;

public class DateMapping extends AbstractStringMapping<Date> {
    public DateMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(Date value) {
        return Dates.LUCENE().format(value);
    }

    @Override
    protected Date fromString(String value) throws ParseException {
        return new DateFormatConverter(sequence(Dates.LUCENE(), Dates.RFC822(), Dates.javaUtilDateToString()).join(Dates.RFC3339().formats())).parse(value);
    }
}
