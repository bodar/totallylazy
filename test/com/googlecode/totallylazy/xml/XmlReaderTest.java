package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Rules;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.StreamingXPath.descendantOld;
import static com.googlecode.totallylazy.xml.StreamingXPath.name;
import static com.googlecode.totallylazy.xml.StreamingXml.currentName;
import static com.googlecode.totallylazy.xml.StreamingXml.text;
import static com.googlecode.totallylazy.xml.XmlReader.xmlReader;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlReaderTest {
    @Test
    @Ignore
    public void supportsLocations() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Sequence<Context> users = XmlReader.locations(new StringReader(xml)).filter(descendant(name("user")));
        Sequence<Map<String, String>> locations = users.
                map(user -> {
                    System.out.println("user = " + user);
                    Sequence<Context> filter = user.relative().
                            filter(descendant(name("first").or(name("dob"))));
                    System.out.println("relative = " + filter.toString("\n"));
                    return map(filter.map(field -> pair(field.name(), field.text())));
                });
        System.out.println(locations.toString("\n"));
    }

    @Test
    public void canStreamIntoAMap() throws Exception {
        String xml = "<stream><user><first>Dan</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Sequence<Location> locations =  forwardOnly(xmlReader(new StringReader(xml)).iterator(descendantOld(name("user"))));
        Sequence<Map<String, String>> users = locations.map(user -> {
            StatefulIterator<Pair<String, String>> iterator = user.stream().
                    iterator(Rules.<Location, Pair<String, String>>rules().
                    addFirst(descendantOld(name("first").or(name("dob"))), field -> pair(currentName(field), text(field))));
            return map(Iterators.map(iterator, pair -> pair));
        });
        assertThat(users, hasExactly(map("first", "Dan", "dob", "1977"), map("first", "Jason", "dob", "1978")));
    }


    @Test
    public void currentlyItEscapesCData() throws Exception {
        String xml = "<stream><![CDATA[Hello <> ]]></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "stream"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &lt;&gt; </stream>"));
    }

    @Test
    public void copiesAllText() throws Exception {
        String xml = "<stream>Hello &amp; World</stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "stream"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream>Hello &amp; World</stream>"));
    }

    @Test
    public void emptyRoot() throws Exception {
        String xml = "<stream/>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "stream"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<stream/>"));
    }

    @Test
    public void singleItem() throws Exception {
        String xml = "<stream><item/></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item/>"));
    }

    @Test
    public void twoItems() throws Exception {
        String xml = "<stream><item id=\"1\"/><item id=\"2\"/></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item id=\"1\"/>"));
        assertThat(Xml.asString(stream.second()), is("<item id=\"2\"/>"));
    }

    @Test
    public void oneItemWithChild() throws Exception {
        String xml = "<stream><item><child/></item></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/></item>"));
    }

    @Test
    public void oneItemWithTwoChildren() throws Exception {
        String xml = "<stream><item><child/><child/></item></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.head()), is("<item><child/><child/></item>"));
    }

    @Test
    public void twoItemsWithChild() throws Exception {
        String xml = "<stream><item><child/></item><item><child/></item></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/></item>"));
    }

    @Test
    public void twoItemsWithTwoChild() throws Exception {
        String xml = "<stream><item><child/><child/></item><item><child/><child/></item></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child/></item>"));
    }

    @Test
    public void supportsAttributes() throws Exception {
        String xml = "<stream><item foo='bar'/></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(1));
        assertThat(Xml.asString(stream.first()), is("<item foo=\"bar\"/>"));
    }

    @Test
    public void supportsAttributesOnNestedStructures() throws Exception {
        String xml = "<stream><item><child foo='bar'/><child/></item><item><child/><child baz='bar'/></item></stream>";
        Sequence<Node> stream = memorise(xmlReader(new StringReader(xml), "item"));
        assertThat(stream.size(), is(2));
        assertThat(Xml.asString(stream.first()), is("<item><child foo=\"bar\"/><child/></item>"));
        assertThat(Xml.asString(stream.second()), is("<item><child/><child baz=\"bar\"/></item>"));
    }
}
