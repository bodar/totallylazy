package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.child;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.name;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.xpath;
import static org.hamcrest.MatcherAssert.assertThat;


public class NodeCreatorTest {
    @Test
    public void works() throws Exception {
        String xml = "<stream>Hello &amp; World</stream>";
        Sequence<Context> contexts = Context.contexts(new StringReader(xml)).filter(xpath(child(name("stream"))));
        Sequence<Node> stream = contexts.map(new NodeCreator());
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &amp; World</stream>"));
    }
}