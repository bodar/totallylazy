package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.time.DateConverter;
import com.googlecode.totallylazy.time.DateFormatConverter;
import com.googlecode.totallylazy.time.Dates;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateMapping implements Mapping<Date> {
    private final StringMapping mapping = new StringMapping();
    private final DateConverter dateConverter;

    public DateMapping(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    public DateMapping(DateFormat... dateFormat) {
        this(new DateFormatConverter(dateFormat));
    }

    public Sequence<Node> to(Document document, String expression, Date value) {
        return mapping.to(document, expression, dateConverter.format(value));
    }

    public Date from(Sequence<Node> nodes) throws ParseException {
        return dateConverter.parse(mapping.from(nodes));
    }

    public static DateMapping defaultFormat() {
        return new DateMapping(DateFormatConverter.defaultConverter());
    }

    public static DateMapping atomDateFormat() {
        return new DateMapping(Dates.RFC3339());
    }

    public static DateMapping rssDateFormat() {
        return new DateMapping(Dates.RFC822());
    }
}
