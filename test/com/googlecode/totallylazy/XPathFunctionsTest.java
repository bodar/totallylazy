package com.googlecode.totallylazy;

import junit.framework.TestCase;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XPathFunctionsTest {
    @Test
    public void supportsJoinStringsInXPath() throws Exception {
        Document document = Xml.document("<root><node>hello</node><node>world</node></root>");
        String joinedStrings = Xml.xpath().evaluate("tl:join-strings(//node/text(), '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }

    @Test
    public void supportsJoinStringsInXPathOnOneNode() throws Exception {
        Document document = Xml.document("<root><node>foo</node></root>");
        String joinedStrings = Xml.xpath().evaluate("tl:join-strings(//node/text(), '--')", document);
        assertThat(joinedStrings, equalTo("foo"));
    }

    @Test
    public void supportsJoinStringsInXPathWithNoNodes() throws Exception {
        Document document = Xml.document("<root/>");
        String joinedStrings = Xml.xpath().evaluate("tl:join-strings(//node/text(), '--')", document);
        assertThat(joinedStrings, equalTo(""));
    }

    @Test
    public void supportsJoinStringsInXPathWithNestedNodes() throws Exception {
        Document document = Xml.document("<root><node><foo>hello</foo></node><node><foo>world</foo></node></root>");
        String joinedStrings = Xml.xpath().evaluate("tl:join-strings(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }
}
