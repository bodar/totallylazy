package com.googlecode.totallylazy;

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

    public static URL url(final String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new LazyException(e);
        }
    }

    public static URI uri(final String uri) {
        return URI.create(uri);
    }


    public static Function1<File, URL> toURL() {
        return new Function1<File, URL>() {
            public URL call(File file) throws Exception {
                return file.toURI().toURL();
            }
        };
    }
}