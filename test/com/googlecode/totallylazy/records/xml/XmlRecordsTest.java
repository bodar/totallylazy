package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Records;
import org.junit.Ignore;

import static com.googlecode.totallylazy.records.xml.Xml.load;

public class XmlRecordsTest extends AbstractRecordsTests{
    public XmlRecordsTest() {
        user = Keyword.keyword("/users/user");
    }

    @Override
    protected Records createRecords() throws Exception {
        return new XmlRecords(load("<users/>"));
    }
}
