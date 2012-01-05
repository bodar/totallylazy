package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlTest {
    @Test
    public void canLoadHtml() throws Exception {
        Document document = Xml.document(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n" +
                        "<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://opengraphprotocol.org/schema/\" xml:lang=\"en-GB\">\n" +
                        "<xh:head><xh:meta content=\"Foo\"/><xh:meta content=\"Bar\"/></xh:head>" +
                        "</xh:html>");

        Sequence<String> values = Xml.selectNodes(document, "//meta/@content").map(Xml.contents());
        assertThat(values, hasExactly("Foo", "Bar"));
    }

    @Test
    public void canEscapeXml() throws Exception {
        assertThat(Xml.escape("& < > ' \" " + new Character((char) 0x80)), is("&amp; &lt; &gt; &#39; &quot; &#128;"));
    }

    @Test
    public void doesNotTruncateString() throws Exception {
        String testString = longStringWithoutEncodedChars();
        assertThat(Xml.escape(testString), is(testString));
    }

    private String longStringWithoutEncodedChars() {
        return repeat("A").take(100).toString("", "", "", Long.MAX_VALUE);
    }

    @Test
    public void supportsXPathExpressionsWithFunctionsThatReturnStrings() throws Exception{
        Document document = Xml.document("<root><child type=\"name\" value=\"bob\"/></root>");
        String value = Xml.selectContents(document, "concat(//child/@type, ':', //child/@value)");
        assertThat(value, is("name:bob"));
    }

    @Test
    public void supportsReturningANumber() throws Exception{
        Document document = Xml.document("<root><child type=\"name\" value=\"bob\"/></root>");
        Number value = Xml.selectNumber(document, "count(//child)");
        assertThat(value, NumberMatcher.is(1));
    }

    @Test
    public void supportsReturningABoolean() throws Exception{
        Document document = Xml.document("<root><child type=\"name\" value=\"bob\"/></root>");
        boolean value = Xml.matches(document, "count(//child) = 1");
        assertThat(value, is(true));
    }
}
