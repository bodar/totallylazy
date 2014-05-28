package com.googlecode.totallylazy;

import org.junit.Test;

import java.net.URI;

import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

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
    public void handlesNonStandardJarUris() throws Exception {
        Uri uri = uri("jar:http://www.foo.com/bar/baz.jar?foo=bar!/COM/foo/Quux.class");
        assertThat(uri.scheme(), is("jar"));
        assertThat(uri.authority(), is("http://www.foo.com/bar/baz.jar?foo=bar"));
        assertThat(uri.path(), is("/COM/foo/Quux.class"));
        assertThat(uri.query(), is(nullValue(String.class)));
        assertThat(uri.fragment(), is(nullValue(String.class)));
        assertThat(uri.toString(), is("jar:http://www.foo.com/bar/baz.jar?foo=bar!/COM/foo/Quux.class"));
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
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").mergePath("/bob").toString(), is("http://www.ics.uci.edu:80/bob?foo=bar#Related"));
        assertThat(uri("foo/bar").mergePath("bob").toString(), is("foo/bob"));
        assertThat(uri("#Related").mergePath("bob").toString(), is("bob#Related"));
        assertThat(uri("relative?foo=bar").mergePath("bob").toString(), is("bob?foo=bar"));
        assertThat(uri("/pub/ietf/uri/?foo=bar#Related").mergePath("bob").toString(), is("/pub/ietf/uri/bob?foo=bar#Related"));
        assertThat(uri("/pub/ietf/uri/?foo=bar#Related").mergePath("/bob").toString(), is("/bob?foo=bar#Related"));
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

    @Test
    public void supportsDroppingParts() throws Exception {
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").dropScheme().dropAuthority(), is(uri("/pub/ietf/uri/?foo=bar#Related")));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").dropScheme().dropAuthority().dropPath(), is(uri("?foo=bar#Related")));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").dropScheme().dropAuthority().dropPath().dropQuery(), is(uri("#Related")));
    }

    @Test
    public void supportsUserInfo() throws Exception {
        assertThat(uri("http://user:password@www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").userInfo(), is("user:password"));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").userInfo("user:password"), is(uri("http://user:password@www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related")));

        assertThat(uri("http://user:password@www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").userInfo(null), is(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related")));
        assertThat(uri("http://user:password@www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").userInfo(""), is(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related")));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").dropUserInfo(), is(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related")));
    }

    @Test
    public void supportsHost() throws Exception {
        assertThat(uri("http:/path").host(), is(nullValue()));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").host(), is("www.ics.uci.edu"));
        assertThat(uri("http://www.ics.uci.edu:80/pub/ietf/uri/?foo=bar#Related").host("example"), is(uri("http://example:80/pub/ietf/uri/?foo=bar#Related")));

        assertThat(uri("http://server/foo").host(null), is(uri("http:/foo")));
        assertThat(uri("http://server/foo").host(""), is(uri("http:/foo")));
        assertThat(uri("http://server/foo").dropHost(), is(uri("http:/foo")));
        assertThat(uri("http://server:8080/foo").dropHost(), is(uri("http:/foo")));
    }

    @Test
    public void supportsPort() throws Exception {
        assertThat(uri("http://server").port(), is(-1));
        assertThat(uri("http://server").port(), is(new URI("http://server").getPort()));
        assertThat(uri("http://server:8080").port(), is(8080));
        assertThat(uri("http://server:8080").port(), is(new URI("http://server:8080").getPort()));

        assertThat(uri("http://server:8080").port(7777), is(uri("http://server:7777")));
        assertThat(uri("http://server:8080").port(-1), is(uri("http://server")));
        assertThat(uri("http://server:8080").dropPort(), is(uri("http://server")));
    }
}
