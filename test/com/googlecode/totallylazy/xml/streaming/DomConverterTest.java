package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.xml.streaming.XPath.child;
import static com.googlecode.totallylazy.xml.streaming.XPath.name;
import static com.googlecode.totallylazy.xml.streaming.XPath.xpath;
import static com.googlecode.totallylazy.xml.streaming.Xml.contexts;
import static org.hamcrest.MatcherAssert.assertThat;


public class DomConverterTest {
    @Test
    public void works() throws Exception {
        String xml = "<stream>Hello &amp; World</stream>";
        Sequence<Context> contexts = contexts(xml).filter(xpath(child(name("stream"))));
        Sequence<Node> stream = contexts.map(DomConverter::convert);
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &amp; World</stream>"));
    }
}