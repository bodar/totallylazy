package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Dates.date;
import static com.googlecode.totallylazy.records.xml.Xml.load;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AtomXmlRecordsTest {
    private static final Keyword<Object> entries = Keyword.keyword("/feed/entry");
    private static final Keyword<Integer> id = Keyword.keyword("id", Integer.class);
    private static final Keyword<String> link = Keyword.keyword("link/@href", String.class);
    private static final Keyword<String> content = Keyword.keyword("content", String.class);
    private static final Keyword<Date> updated = Keyword.keyword("updated", Date.class);

    @Test
    public void canGetElements() throws Exception {
        Records records = new XmlRecords(load(XML));
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

    private static final String LINK = "http://localhost:10010/somePath";

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
