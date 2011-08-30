package com.googlecode.totallylazy;

import com.googlecode.totallylazy.regex.Regex;

import java.util.regex.MatchResult;

public class Uri {
    public static Regex RFC3986 = Regex.regex("^(?:([^:/?\\#]+):)?(?://([^/?\\#]*))?([^?\\#]*)(?:\\?([^\\#]*))?(?:\\#(.*))?");
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
        MatchResult result = RFC3986.match(value);
        scheme = result.group(1);
        authority = result.group(2);
        path = result.group(3);
        query = result.group(4);
        fragment = result.group(5);
    }

    public static Uri uri(CharSequence value) {
        return new Uri(value);
    }

    public String scheme(){
        return scheme;
    }

    public Uri scheme(String value){
        return new Uri(value, authority, path, query, fragment);
    }

    public String authority(){
        return authority;
    }

    public Uri authority(String value){
        return new Uri(scheme, value, path, query, fragment);
    }

    public String path(){
        return path;
    }

    public Uri path(String value){
        return new Uri(scheme, authority, mergePath(this, value), query, fragment);
    }

    private static String mergePath(Uri baseUri, String referencePath){
        if(referencePath.startsWith("/") || baseUri.path() == null || baseUri.isRelative()){
            return referencePath;
        }

        if(baseUri.path().equals(Strings.EMPTY)){
            return "/" + referencePath;
        }

        return baseUri.path().replaceFirst("/([^/]*)$", "/" + referencePath);

    }

    public String query(){
        return query;
    }

    public Uri query(String value){
        return new Uri(scheme, authority, path, value, fragment);
    }

    public String fragment(){
        return fragment;
    }

    public Uri fragment(String value){
        return new Uri(scheme, authority, path, query, value);
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
        StringBuilder builder = new StringBuilder();
        if(scheme != null){
            builder.append(scheme).append(":");
        }
        if(authority != null){
            builder.append("//").append(authority);
        }
        builder.append(path);
        if(query != null){
            builder.append("?").append(query);
        }
        if(fragment != null){
            builder.append("#").append(fragment);
        }
        return builder.toString();
    }

    public boolean isAbsolute() {
        return path.startsWith("/");
    }

    public boolean isRelative() {
        return !isAbsolute();
    }
}
