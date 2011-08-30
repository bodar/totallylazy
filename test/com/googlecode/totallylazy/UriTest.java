package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Uri.uri;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UriTest {
    @Test
    public void correctlyParsesUrls() throws Exception {
        Uri uri = uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related");
        assertThat(uri.scheme(), is("http"));
        assertThat(uri.authority(), is("www.ics.uci.edu:80"));
        assertThat(uri.path(), is("/pub/ietf/uri/"));
        assertThat(uri.query(), is("foo=bar"));
        assertThat(uri.fragment(), is("Related"));
    }
    
    @Test
    public void supportsToString() throws Exception {
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").toString(), is("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related"));
        assertThat(uri("file:///pub/ietf/uri/").toString(), is("file:///pub/ietf/uri/"));
        assertThat(uri("file:/pub/ietf/uri/").toString(), is("file:/pub/ietf/uri/"));
        assertThat(uri("pub/ietf/uri/").toString(), is("pub/ietf/uri/"));
        assertThat(uri("?foo").toString(), is("?foo"));
        assertThat(uri("#bar").toString(), is("#bar"));
    }

    @Test
    public void supportsMergingPaths() throws Exception {
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").path("/bob").toString(), is("http://www.ics.uci.edu:80/bob?foo=bar#Related"));
        assertThat(uri("#Related").path("bob").toString(), is("bob#Related"));
        assertThat(uri("relative?foo=bar").path("bob").toString(), is("bob?foo=bar"));
        assertThat(uri("/pub/ietf/uri/?foo=bar#Related").path("bob").toString(), is("/pub/ietf/uri/bob?foo=bar#Related"));
    }

    @Test
    public void supportsDetectingAbsolutePaths() throws Exception {
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").isAbsolute(), is(true));
        assertThat(uri("#Related").isAbsolute(), is(false));
        assertThat(uri("#Related").isRelative(), is(true));
        assertThat(uri("relative?foo=bar").isAbsolute(), is(false));
        assertThat(uri("relative?foo=bar").isRelative(), is(true));
        assertThat(uri("/pub/ietf/uri/?foo=bar#Related").isAbsolute(), is(true));
        assertThat(uri("/pub/ietf/uri/?foo=bar#Related").isRelative(), is(false));
    }
}
