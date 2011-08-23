package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Keywords;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.xml.mappings.DateMapping;
import com.googlecode.totallylazy.records.xml.mappings.Mappings;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static com.googlecode.totallylazy.Dates.date;
import static com.googlecode.totallylazy.URLs.uri;
import static com.googlecode.totallylazy.records.xml.Xml.document;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AtomXmlRecordsTest {
    private static final Keyword<Object> entries = Keywords.keyword("/feed/entry");
    private static final Keyword<Integer> id = Keywords.keyword("id", Integer.class);
    private static final Keyword<URI> link = Keywords.keyword("link/@href", URI.class);
    private static final Keyword<String> content = Keywords.keyword("content", String.class);
    private static final Keyword<Date> updated = Keywords.keyword("updated", Date.class);

    @Test
    public void canGetElements() throws Exception {
        Records records = new XmlRecords(document(XML), new Mappings().add(Date.class, DateMapping.atomDateFormat()));
        records.define(entries, id, link, content, updated);
        Record record = records.get(entries).head();
        assertThat(record.get(id), is(ID));
        assertThat(record.get(link), is(LINK));
        assertThat(record.get(content), is(CONTENT));
        assertThat(record.get(updated), is(date(2011, 7, 19, 12, 43, 26)));
    }

    public static final Integer ID = 1234;

    private static final String CONTENT = "<event>" +
            "<source>blah</source>" +
            "<payload>foo</payload>" +
            "</event>";

    private static final URI LINK = uri("http://localhost:10010/somePath");

    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<feed>" +
            "  <title>Some feed</title>" +
            "  <entry>" +
            "    <title>Some entry</title>" +
            "    <link href=\"" + LINK + "\" />" +
            "    <id>" + ID + "</id>" +
            "<content type=\"text/xml\">" +
            CONTENT +
            "</content>" +
            "    <updated>2011-07-19T12:43:26Z</updated>" +
            "    <summary type=\"text\">Summary</summary>" +
            "  </entry>" +
            "</feed>";
}
