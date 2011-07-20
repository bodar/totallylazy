package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapping implements Mapping<Date> {
    private final StringMapping mapping = new StringMapping();
    private final SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Sequence<Node> to(Document document, String expression, Date value) {
        return mapping.to(document, expression, rfc3339.format(value));
    }

    public Date from(Sequence<Node> nodes) throws ParseException {
        return rfc3339.parse(mapping.from(nodes));
    }
}
