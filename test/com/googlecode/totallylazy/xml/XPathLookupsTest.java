package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Maps;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.xml.Xml.document;
import static com.googlecode.totallylazy.xml.Xml.xpath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XPathLookupsTest {
    @Test
    public void supportsLookups() throws Exception {
        Map<String, String> data = new HashMap<String, String>() {{
            put("Dan", "Jorge");
        }};
        XPathLookups.setLookup("name", Maps.functions.getFrom(data));
        Document document = document("<user>Dan</user>");
        assertThat(xpath().evaluate("tl:lookup('name', //user)", document), equalTo("Jorge"));
    }
}
