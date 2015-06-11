package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.TimeReport;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlTest {
    @Test
    public void shouldSupportRealWorldNamespacesAndComplexQueries() throws Exception {
        Document bt = Xml.document("<?xml version=\"1.0\"?>\n" +
                "<or:OrderStatusUpdate xmlns:gea=\"urn:uk.org.telcob2b/tML/Fulfilment/GEA-v16-0\" xmlns:odr=\"urn:uk.org.telcob2b/tML/BT-OrderResponse-v16-0\" xmlns:or=\"urn:com.openreach.Fulfilment2-v16-0\" xmlns:utcc=\"urn:uk.org.telcob2b/tML/ukt-CommonComponents-v16-0\" xmlns=\"\">\n" +
                "  <odr:OrderResponse>\n" +
                "    <utcc:RespondedOrderLine>\n" +
                "      <utcc:OrderLineMessageInfo>\n" +
                "        <utcc:MessageInfo>\n" +
                "          <utcc:CompletionCode>510</utcc:CompletionCode>\n" +
                "        </utcc:MessageInfo>\n" +
                "      </utcc:OrderLineMessageInfo>\n" +
                "    </utcc:RespondedOrderLine>\n" +
                "  </odr:OrderResponse>\n" +
                "</or:OrderStatusUpdate>\n");

        assertThat(Xml.selectNodes(bt, "tl:trim-and-join(//MessageInfo/CompletionCode, \"\\n\")").size(), is(1));
    }

    @Test
    @Ignore("manual test")
    public void xpathIsPrettyFast() throws Exception {
        final Document document = example();
        TimeReport report = TimeReport.time(1000, () -> {
            return Xml.selectNodes(document, "//meta/@content");
        });
        System.out.println(report);
    }

    @Test
    public void canLoadHtml() throws Exception {
        Document document = example();

        assertThat(Xml.selectNodes(document, "//meta/@content").map(Xml.contents()), hasExactly("Foo", "Bar"));
        assertThat(Xml.selectContents(document, "descendant::boo"), is("far"));
        assertThat(Xml.selectContents(document, "descendant::foo"), is("baz"));
    }

    private Document example() {
        return Xml.document(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">\n" +
                        "<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://opengraphprotocol.org/schema/\" xml:lang=\"en-GB\" xmlns=\"\">\n" +
                        "<xh:head><xh:meta content=\"Foo\"/><xh:meta content=\"Bar\"/></xh:head>" +
                        "<foo>baz</foo>" +
                        "<og:boo>far</og:boo>" +
                        "</xh:html>");
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
        return repeat("A").take(100).toString("");
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