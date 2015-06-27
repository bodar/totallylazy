package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.xml.Xml;
import com.googlecode.totallylazy.functions.TimeReport;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.functions.Functions.option;
import static com.googlecode.totallylazy.functions.Functions.or;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.xml.streaming.XPath.attribute;
import static com.googlecode.totallylazy.xml.streaming.XPath.child;
import static com.googlecode.totallylazy.xml.streaming.XPath.descendant;
import static com.googlecode.totallylazy.xml.streaming.XPath.name;
import static com.googlecode.totallylazy.xml.streaming.XPath.xpath;
import static com.googlecode.totallylazy.xml.streaming.Xml.contexts;
import static com.googlecode.totallylazy.xml.streaming.Xml.nodes;

public class XmlTest {
    @Test
    public void supportsLocations() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Sequence<Map<String, String>> users = contexts(xml).filter(xpath(descendant(name("user")))).
                map(user -> Maps.<String,String>map(user.relative().collect(
                        xpath(child("first")), field -> pair("name", field.text()),
                        xpath(child("dob")), field -> pair(field.name(), field.text())
                )));
        assertThat(users, is(sequence(map("name", "Dan & Bod", "dob", "1977"), map("name", "Jason", "dob", "1978"))));
    }

    @Test
    @Ignore("manual test")
    public void xpathIsPrettyFast() throws Exception {
        final Sequence<Context> document = contexts("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n" +
                "<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://opengraphprotocol.org/schema/\" xml:lang=\"en-GB\" xmlns=\"\">\n" +
                "<xh:head><xh:meta content=\"Foo\"/><xh:meta content=\"Bar\"/></xh:head>" +
                "<foo>baz</foo>" +
                "<og:boo>far</og:boo>" +
                "</xh:html>");
        TimeReport report = TimeReport.time(1000, () -> {
            return document.filter(xpath(descendant("meta"))).map(context -> context.attributes().get("content")).realise();
        });
        System.out.println(report);
    }

    @Test
    @Ignore("manual test")
    public void canLoadCitations() throws Exception {
        String path = "an-article-with-many-references.xml";
        try (FileReader fileReader = new FileReader(path) ) {
            Sequence<Context> document = contexts(new BufferedReader(fileReader));
            TimeReport report = TimeReport.time(10, () -> document.
                    filter(xpath(descendant("Citation"), child("BibArticle"))).
                    map(citation -> Maps.map(citation.relative().collect(
                        option(xpath(child("ArticleTitle")), field -> pair("title", field.text())),
                        option(xpath(child("BibAuthorName")), field -> pair("authors", field.text())),
                        option(xpath(child("VolumeID")), field -> pair("volume", field.text())),
                        option(xpath(child("Year")), field -> pair("pubYear", field.text())),
                        option(xpath(child("JournalTitle")), field -> pair("journalTitle", field.text())),
                        option(xpath(child(name("Occurrence").and(attribute("Type", is("DOI")))), child("Handle")), field -> pair("doi", field.text()))
                ))).realise());
            System.out.println(report);
        }
    }

    @Test
    public void currentlyItEscapesCData() throws Exception {
        String xml = "<stream><![CDATA[Hello <> ]]></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "stream");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &lt;&gt; </stream>"));
    }

    @Test
    public void copiesAllText() throws Exception {
        String xml = "<stream>Hello &amp; World</stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "stream");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &amp; World</stream>"));
    }

    @Test
    public void emptyRoot() throws Exception {
        String xml = "<stream/>";
        Sequence<Node> stream = nodes(new StringReader(xml), "stream");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream/>"));
    }

    @Test
    public void singleItem() throws Exception {
        String xml = "<stream><item/></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item/>"));
    }

    @Test
    public void twoItems() throws Exception {
        String xml = "<stream><item id=\"1\"/><item id=\"2\"/></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item id=\"1\"/>"));
        assertThat(Xml.asString(stream.second()), is("<item id=\"2\"/>"));
    }

    @Test
    public void oneItemWithChild() throws Exception {
        String xml = "<stream><item><child/></item></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/></item>"));
    }

    @Test
    public void oneItemWithTwoChildren() throws Exception {
        String xml = "<stream><item><child/><child/></item></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/><child/></item>"));
    }

    @Test
    public void twoItemsWithChild() throws Exception {
        String xml = "<stream><item><child/></item><item><child/></item></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/></item>"));
    }

    @Test
    public void twoItemsWithTwoChild() throws Exception {
        String xml = "<stream><item><child/><child/></item><item><child/><child/></item></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child/></item>"));
    }

    @Test
    public void supportsAttributes() throws Exception {
        String xml = "<stream><item foo='bar'/></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.first()), is("<item foo=\"bar\"/>"));
    }

    @Test
    public void supportsAttributesOnNestedStructures() throws Exception {
        String xml = "<stream><item><child foo='bar'/><child/></item><item><child/><child baz='bar'/></item></stream>";
        Sequence<Node> stream = nodes(new StringReader(xml), "item");
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child foo=\"bar\"/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child baz=\"bar\"/></item>"));
    }
}
