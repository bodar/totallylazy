package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Maps;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Xml.document;
import static com.googlecode.totallylazy.Xml.xpath;
import static com.googlecode.totallylazy.Assert.assertThat;

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
