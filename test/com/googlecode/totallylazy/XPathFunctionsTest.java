package com.googlecode.totallylazy;

import org.junit.Test;
import org.w3c.dom.Document;

import static com.googlecode.totallylazy.Xml.document;
import static com.googlecode.totallylazy.Xml.xpath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XPathFunctionsTest {

    @Test
    public void supportsNewLineChar() throws Exception {
        Document document = document("<root><node>hello</node><node>world</node></root>");
        String joinedStrings = xpath().evaluate("tl:join-strings(//node, '\\n')", document);
        assertThat(joinedStrings, equalTo("hello\nworld"));
    }

    @Test
    public void supportsJoinStringsInXPath() throws Exception {
        Document document = document("<root><node>hello</node><node>world</node></root>");
        String joinedStrings = xpath().evaluate("tl:join-strings(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }

    @Test
    public void supportsJoinStringsInXPathOnOneNode() throws Exception {
        Document document = document("<root><node>foo</node></root>");
        String joinedStrings = xpath().evaluate("tl:join-strings(//node, '--')", document);
        assertThat(joinedStrings, equalTo("foo"));
    }

    @Test
    public void supportsJoinStringsInXPathWithNoNodes() throws Exception {
        Document document = document("<root/>");
        String joinedStrings = xpath().evaluate("tl:join-strings(//node, '--')", document);
        assertThat(joinedStrings, equalTo(""));
    }

    @Test
    public void supportsJoinStringsInXPathWithNestedNodes() throws Exception {
        Document document = document("<root><node><foo>hello</foo></node><node><foo>world</foo></node></root>");
        String joinedStrings = xpath().evaluate("tl:join-strings(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }
}
