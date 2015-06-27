package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Classes;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.functions.Function1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.googlecode.totallylazy.Strings.EMPTY;

@SuppressWarnings("unused")
public class URLs {
    public static URL packageUrl(final Class<?> aClass) {
        String name = aClass.getSimpleName() + ".class";
        return url(aClass.getResource(name).toString().replace(name, EMPTY));
    }

    public static URL rootUrl(final Class<?> aClass) {
        String filename = Classes.filename(aClass);
        return url(aClass.getResource("/" + filename).toString().replace(filename, EMPTY));
    }

    public static URL url(final String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw LazyException.lazyException(e);
        }
    }

    public static URI uri(final String uri) {
        return URI.create(uri);
    }


    public static Function1<File, URL> toURL() {
        return file -> file.toURI().toURL();
    }

    public static URL url(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw LazyException.lazyException(e);
        }
    }
}