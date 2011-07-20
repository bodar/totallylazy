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

    @Override @Ignore
    public void supportsIsNullAndNotNull() throws Exception {
    }

    @Override @Ignore
    public void supportsReduce() throws Exception {
    }

    @Override @Ignore
    public void supportsUpdating() throws Exception {
    }

    @Override @Ignore
    public void supportsRemove() throws Exception {
    }
}
