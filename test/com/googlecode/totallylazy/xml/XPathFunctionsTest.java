package com.googlecode.totallylazy.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;

import static com.googlecode.totallylazy.Xml.document;
import static com.googlecode.totallylazy.Xml.xpath;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XPathFunctionsTest {
    @Test
    public void supportsNewLineChar() throws Exception {
        Document document = document("<root><node>hello</node><node>world</node></root>");
        String joinedStrings = xpath().evaluate("tl:string-join(//node, '\\n')", document);
        assertThat(joinedStrings, equalTo("hello\nworld"));
    }

    @Test
    public void supportsJoinStringsInXPath() throws Exception {
        Document document = document("<root><node>hello</node><node>world</node></root>");
        String joinedStrings = xpath().evaluate("tl:string-join(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }

    @Test
    public void supportsJoinStringsInXPathOnOneNode() throws Exception {
        Document document = document("<root><node>foo</node></root>");
        String joinedStrings = xpath().evaluate("tl:string-join(//node, '--')", document);
        assertThat(joinedStrings, equalTo("foo"));
    }

    @Test
    public void supportsJoinStringsInXPathWithNoNodes() throws Exception {
        Document document = document("<root/>");
        String joinedStrings = xpath().evaluate("tl:string-join(//node, '--')", document);
        assertThat(joinedStrings, equalTo(""));
    }

    @Test
    public void supportsJoinStringsInXPathWithNestedNodes() throws Exception {
        Document document = document("<root><node><foo>hello</foo></node><node><foo>world</foo></node></root>");
        String joinedStrings = xpath().evaluate("tl:string-join(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }

    @Test
    public void supportsStringTrimAndJoin() throws Exception {
        Document document = document("<root><node><foo>hello </foo></node><node><foo> world</foo></node></root>");
        String joinedStrings = xpath().evaluate("tl:trim-and-join(//node, '--')", document);
        assertThat(joinedStrings, equalTo("hello--world"));
    }

    @Test
    public void supportsIf() throws Exception {
        Document document = document("<root><note>Hello Dan</note><other>Hello Tom</other></root>");
        assertThat(xpath().evaluate("tl:if(//note[text() = 'Hello Dan'], 'Matched', 'Not Matched' )", document), equalTo("Matched"));
        assertThat(xpath().evaluate("tl:if(//note[not(text() = 'Hello Dan')], 'Matched', //other )", document), equalTo("Hello Tom"));
    }

    @Test
    public void supportsOr() throws Exception {
        Document document = document("<root><note>Hello Dan</note><other>Hello Tom</other><user></user></root>");
        assertThat(xpath().evaluate("tl:or(//note, //other)", document), equalTo("Hello Dan"));
        assertThat(xpath().evaluate("tl:or(//notPresent, //other)", document), equalTo("Hello Tom"));
        assertThat(xpath().evaluate("tl:or(//user/text(), //note, //other)", document), equalTo("Hello Dan"));
    }

    @Test
    public void supportsTokenize() throws Exception {
        Document document = document("<root><note>Hello Dan</note><other>Hello Tom</other><user></user></root>");
        assertThat(xpath().evaluate("tl:tokenize(//note/text(), '\\s')[1]", document), equalTo("Hello"));
        assertThat(xpath().evaluate("tl:tokenize(//note, '\\s')[2]", document), equalTo("Dan"));
        assertThat(xpath().evaluate("tl:tokenize(//text(), '\\s')[4]", document), equalTo("Tom"));
    }

}
