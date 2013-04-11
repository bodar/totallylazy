package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Functions;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.googlecode.totallylazy.Xml.document;
import static com.googlecode.totallylazy.Xml.xpath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XPathLookupsTest {
    @Test
    public void supportsLookups() throws Exception {
        XPathLookups.setLookup("name", Functions.<String, String>constant("Jorge"));
        Document document = document("<user>Dan</user>");
        assertThat(xpath().evaluate("tl:lookup('name', //user)", document), equalTo("Jorge"));
    }

}
