package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Dates;
import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapping implements Mapping<Date> {
    private final StringMapping mapping = new StringMapping();
    private final DateFormat dateFormat;

    public DateMapping(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Sequence<Node> to(Document document, String expression, Date value) {
        return mapping.to(document, expression, dateFormat.format(value));
    }

    public Date from(Sequence<Node> nodes) throws ParseException {
        return dateFormat.parse(mapping.from(nodes));
    }

    public static DateMapping defaultFormat() {
        return new DateMapping(new SimpleDateFormat());
    }

    public static DateMapping atomDateFormat() {
        return new DateMapping(Dates.RFC3339());
    }

    public static DateMapping rssDateFormat() {
        return new DateMapping(Dates.RFC822());
    }
}
