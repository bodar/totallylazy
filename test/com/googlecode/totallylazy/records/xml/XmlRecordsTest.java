package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Keywords;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.googlecode.totallylazy.records.xml.Xml.load;

public class XmlRecordsTest extends AbstractRecordsTests<XmlRecords>{
    private Document document;

    public XmlRecordsTest() {
        people = Keywords.keyword("/data/user");
        books = Keywords.keyword("/data/book");
    }

    @Override
    protected XmlRecords createRecords() throws Exception {
        document = load("<data/>");
        return new XmlRecords(document);
    }

    @Test
    public void showGeneratedXml() throws Exception {
        System.out.println(Xml.format(document.getDocumentElement()));
    }
}
