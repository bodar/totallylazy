package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.regex.Regex;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.Strings.isEmpty;

public class Uri implements Comparable<Uri> {
    public static final Regex JAR_URL = Regex.regex("jar:([^!]+)!(/.*)");
    public static final Regex RFC3986 = Regex.regex("^(?:([^:/?\\#]+):)?(?://([^/?\\#]*))?([^?\\#]*)(?:\\?([^\\#]*))?(?:\\#(.*))?");
    public static final String JAR_SCHEME = "jar";
    public static final String FILE_SCHEME = "file";
    private final String scheme;
    private final String authority;
    private final String path;
    private final String query;
    private final String fragment;

    public Uri(String scheme, String authority, String path, String query, String fragment) {
        this.scheme = scheme;
        this.authority = authority;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
    }

    public Uri(CharSequence value) {
        if (JAR_URL.matches(value)) {
            MatchResult jar = JAR_URL.match(value);
            scheme = JAR_SCHEME;
            authority = jar.group(1);
            path = jar.group(2);
            query = null;
            fragment = null;
        } else {
            MatchResult result = RFC3986.match(value);
            scheme = result.group(1);
            authority = result.group(2);
            path = result.group(3);
            query = result.group(4);
            fragment = result.group(5);
        }
    }

    public static Uri uri(File value) {
        return uri(value.toURI());
    }

    public static Uri uri(CharSequence value) {
        return new Uri(value);
    }

    public static Uri uri(URL value) {
        return new Uri(value.toString());
    }

    public static Uri uri(URI value) {
        return new Uri(value.toString());
    }

    public static Uri packageUri(Class<?> aClass) {
        return uri(URLs.packageUrl(aClass));
    }

    public static Uri rootUri(Class<?> aClass) {
        return uri(URLs.rootUrl(aClass));
    }

    public String scheme() {
        return scheme;
    }

    public Uri scheme(String value) {
        return new Uri(value, authority, path, query, fragment);
    }

    public Uri dropScheme() {
        return scheme(null);
    }

    public String authority() {
        return authority;
    }

    public Uri authority(String value) {
        return new Uri(scheme, value, path, query, fragment);
    }

    private Authority Authority() {
        return Authority.authority(authority);
    }

    private Uri Authority(Authority value) {
        return authority(value.toString());
    }

    public Uri dropAuthority() {
        return authority(null);
    }

    public String userInfo() {
        return Authority().userInfo();
    }

    public Uri userInfo(String value) {
        return Authority(Authority().userInfo(value));
    }

    public Uri dropUserInfo() {
        return userInfo(null);
    }

    public String host() {
        return Authority().host();
    }

    public Uri host(String value) {
        return Authority(Authority().host(value));
    }

    public Uri dropHost() {
        return host(null);
    }

    public int port() {
        return Authority().port();
    }

    public Uri port(int value) {
        return Authority(Authority().port(value));
    }

    public Uri dropPort() {
        return port(-1);
    }

    public String path() {
        return path;
    }

    public Uri path(String value) {
        return new Uri(scheme, authority, value, query, fragment);
    }

    public Uri dropPath() {
        return path(Strings.EMPTY);
    }

    public Uri mergePath(String value) {
        if (value.startsWith("/")) {
            return path(value);
        }

        if (authority() != null && path().equals(Strings.EMPTY)) {
            return path("/" + value);
        }

        return path(path().replaceFirst("([^/]*)$", value));
    }

    public String query() {
        return query;
    }

    public Uri query(String value) {
        return new Uri(scheme, authority, path, Strings.isBlank(value) ? null : value, fragment);
    }

    public Uri dropQuery() {
        return query(null);
    }

    public String fragment() {
        return fragment;
    }

    public Uri fragment(String value) {
        return new Uri(scheme, authority, path, query, value);
    }

    public Uri dropFragment() {
        return fragment(null);
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof Uri && toString().equals(o.toString());
    }

    @Override
    public final int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        if (JAR_SCHEME.equals(scheme)) {
            return String.format("%s:%s!%s", JAR_SCHEME, authority, path);
        }
        return standardToString();
    }

    private String standardToString() {
        StringBuilder builder = new StringBuilder();
        if (scheme != null) {
            builder.append(scheme).append(":");
        }
        if (authority != null) {
            builder.append("//").append(authority);
        }
        builder.append(path);
        if (query != null) {
            builder.append("?").append(query);
        }
        if (fragment != null) {
            builder.append("#").append(fragment);
        }
        return builder.toString();
    }

    public URL toURL() {
        return URLs.url(toString());
    }

    public URI toURI() {
        return URLs.uri(toString());
    }

    public boolean isFullyQualified() {
        return !isEmpty(authority);
    }

    public boolean isAbsolute() {
        return path.startsWith("/");
    }

    public boolean isRelative() {
        return !isAbsolute();
    }

    @Override
    public int compareTo(Uri other) {
        return toString().compareTo(other.toString());
    }

    public File toFile() {
        return new File(toURI());
    }

    public Uri removeDotSegments() {
        if(Strings.isEmpty(path)) return this;
        return path(DotSegments.remove(path));
    }

    public static class functions {
        public static Function1<String, Uri> uri = Uri::uri;

        public static Function1<String, Uri> uri() {
            return uri;
        }

        public static final Function1<Uri, String> path = Uri::path;

        public static final Function1<Uri, String> host = Uri::host;

        public static Function1<Uri, Uri> host(final String newHost) {
            return uri1 -> uri1.host(newHost);
        }

        public static Function1<Uri, URL> URL = Uri::toURL;
    }

    static class Authority {
        private static final Regex AUTHORITY = Regex.regex("(?:([^@]+)@)?([^:]+)(?:\\:([\\d]+))?");
        private final String userInfo;
        private final String host;
        private final int port;

        private Authority(String userInfo, String host, int port) {
            this.userInfo = userInfo;
            this.host = host;
            this.port = port;
        }

        static Authority authority(String authority) {
            if (authority == null) return authority(null, null, null);
            MatchResult match = AUTHORITY.match(authority);
            return authority(match.group(1), match.group(2), match.group(3));
        }

        static Authority authority(String userInfo, String host, String port) {
            return authority(userInfo, host, port == null ? -1 : Integer.parseInt(port));
        }

        static Authority authority(String userInfo, String host, int port) {
            return new Authority(userInfo, host, port);
        }

        String userInfo() {
            return userInfo;
        }

        Authority userInfo(String value) {
            return authority(value, host, port);
        }

        String host() {
            return host;
        }

        Authority host(String value) {
            return authority(userInfo, value, port);
        }

        int port() {
            return port;
        }

        Authority port(int value) {
            return authority(userInfo, host, value);
        }

        @Override
        public String toString() {
            if (isEmpty(host)) return null;
            StringBuilder builder = new StringBuilder();
            if (!isEmpty(userInfo)) builder.append(userInfo).append("@");
            builder.append(host);
            if (port != -1) builder.append(":").append(port);
            return builder.toString();
        }
    }
}