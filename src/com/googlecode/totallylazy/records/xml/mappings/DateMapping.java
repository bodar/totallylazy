package com.googlecode.totallylazy.records.xml.mappings;

import com.googlecode.totallylazy.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Date;

public class DateMapping implements Mapping<Date> {
    private final LongMapping mapping = new LongMapping();

    public Sequence<Node> to(Document document, String expression, Date value) {
        return mapping.to(document, expression, value.getTime());
    }

    public Date from(Sequence<Node> nodes) {
        return new Date(mapping.from(nodes));
    }
}
