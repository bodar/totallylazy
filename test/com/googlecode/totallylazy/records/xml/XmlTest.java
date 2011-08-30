package com.googlecode.totallylazy.records.xml;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
