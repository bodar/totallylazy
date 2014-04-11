package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Xml;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;

import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlReaderTest {
    @Test
    public void emptyRoot() throws Exception {
        String xml = "<stream/>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "stream"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream/>"));
    }

    @Test
    public void singleItem() throws Exception {
        String xml = "<stream><item/></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item/>"));
    }

    @Test
    public void twoItems() throws Exception {
        String xml = "<stream><item/><item/></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item/>"));
        assertThat(Xml.asString(stream.second()), is("<item/>"));

    }

    @Test
    public void oneItemWithChild() throws Exception {
        String xml = "<stream><item><child/></item></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/></item>"));
    }

    @Test
    public void oneItemWithTwoChildren() throws Exception {
        String xml = "<stream><item><child/><child/></item></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/><child/></item>"));
    }

    @Test
    public void twoItemsWithChild() throws Exception {
        String xml = "<stream><item><child/></item><item><child/></item></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/></item>"));
    }

    @Test
    public void twoItemsWithTwoChild() throws Exception {
        String xml = "<stream><item><child/><child/></item><item><child/><child/></item></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child/></item>"));
    }

    @Test
    public void supportsAttributes() throws Exception {
        String xml = "<stream><item foo='bar'/></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.first()), is("<item foo=\"bar\"/>"));
    }

    @Test
    public void supportsAttributesOnNestedStructures() throws Exception {
        String xml = "<stream><item><child foo='bar'/><child/></item><item><child/><child baz='bar'/></item></stream>";
        Seq<Node> stream = memorise(XmlReader.xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child foo=\"bar\"/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child baz=\"bar\"/></item>"));
    }
}
