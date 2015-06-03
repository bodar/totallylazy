package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Xml;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.StringReader;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.attribute;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.child;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.descendant;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.name;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.text;
import static com.googlecode.totallylazy.xml.streaming.StreamingXPath.xpath;

public class ContextTest {
    @Test
    public void unfilteredHasCorrectNumberOfContexts() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";

        Sequence<Context> locations = Context.contexts(new StringReader(xml));
        org.junit.Assert.assertEquals(locations.toString("\n"), "<stream>\n" +
                "<stream>/<user>\n" +
                "<stream>/<user>/<first>\n" +
                "<stream>/<user>/<first>/Dan & Bod\n" +
                "<stream>/<user>/<dob>\n" +
                "<stream>/<user>/<dob>/1977\n" +
                "<stream>/<user>\n" +
                "<stream>/<user>/<first>\n" +
                "<stream>/<user>/<first>/Jason\n" +
                "<stream>/<user>/<dob>\n" +
                "<stream>/<user>/<dob>/1978");
    }

    @Test
    public void unfilteredStillWorksEvenWhenTextHasNestedElement() throws Exception {
        String xml = "<stream>Hello <b>Dan</b> Bodart</stream>";
        Sequence<Context> locations = Context.contexts(new StringReader(xml));
        org.junit.Assert.assertEquals(locations.toString("\n"), "<stream>\n" +
                "<stream>/Hello \n" +
                "<stream>/<b>\n" +
                "<stream>/<b>/Dan\n" +
                "<stream>/ Bodart");
    }


    @Test
    public void shouldSupportChild() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "child::*");
        Sequence<Context> locations = Context.contexts(new StringReader(xml)).filter(xpath(child(name("*"))));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.name(), is(root.getNodeName()));
    }

    @Test
    public void shouldSupportDescendant() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "descendant::*");

        Sequence<Context> locations = Context.contexts(new StringReader(xml)).filter(xpath(descendant(name("*"))));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.name(), is(root.getNodeName()));
    }

    @Test
    public void shouldSupportDescendantWithNames() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "descendant::user");

        Sequence<Context> locations = Context.contexts(new StringReader(xml)).filter(xpath(descendant(name("user"))));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.name(), is(root.getNodeName()));
    }

    @Test
    public void shouldSupportAttributes() throws Exception {
        String xml = "<stream><user first='Dan'></user><user first='Jason'></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "descendant::user[@first='Dan']");

        Sequence<Context> locations = Context.contexts(new StringReader(xml)).
                filter(xpath(descendant(name("user").and(attribute("first", is("Dan"))))));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.name(), is(root.getNodeName()));
    }


    @Test
    public void shouldSupportTextNodes() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "descendant::text()");

        Sequence<Context> locations = Context.contexts(new StringReader(xml)).filter(xpath(descendant(text())));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.text(), is(root.getTextContent()));
    }

    @Test
    public void canGetTextOfElement() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "descendant::user");

        Sequence<Context> locations = Context.contexts(new StringReader(xml)).filter(xpath(descendant(name("user"))));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.text(), is(root.getTextContent()));
    }

    @Test
    public void canSelectTextNodes() throws Exception {
        String xml = "<stream>Hello <b>Dan</b> Bodart</stream>";
        Document document = Xml.document(xml);

        Sequence<Node> nodes = Xml.selectNodes(document, "child::stream/child::text()");

        Sequence<Context> all = Context.contexts(new StringReader(xml));
        Sequence<Context> locations = all.
                filter(xpath(child(name("stream")), child(text())));
        assertThat(locations.size(), is(nodes.size()));

        Node root = nodes.head();
        Context rootContext = locations.head();
        assertThat(rootContext.text(), is(root.getTextContent()));
    }

    @Test
    public void supportsRelative() throws Exception {
        String xml = "<stream><user><first>Dan &amp; Bod</first><dob>1977</dob></user><user><first>Jason</first><dob>1978</dob></user></stream>";
        Document document = Xml.document(xml);

        Sequence<Node> userNodes = Xml.selectNodes(document, "descendant::user");

        Sequence<Context> usersContexts = Context.contexts(new StringReader(xml)).
                filter(xpath(descendant(name("user"))));
        assertThat(usersContexts.size(), is(userNodes.size()));

        Context danContext = usersContexts.head();
        Sequence<Context> relative = danContext.relative();
        assertThat(relative.toString("\n"), is("<first>\n" +
                "<first>/Dan & Bod\n" +
                "<dob>\n" +
                "<dob>/1977"));
    }

    @Test
    public void oneItemWithTwoChildren() throws Exception {
        String xml = "<stream><item><child/><child/></item></stream>";

        Sequence<Context> locations = Context.contexts(new StringReader(xml));
        org.junit.Assert.assertEquals(locations.toString("\n"), "<stream>\n" +
                "<stream>/<item>\n" +
                "<stream>/<item>/<child>\n" +
                "<stream>/<item>/<child>");
    }

}