package com.googlecode.totallylazy;

import java.net.MalformedURLException;
import java.net.URL;

import static com.googlecode.totallylazy.Strings.EMPTY;

public class URLs {
    public static URL packageUrl(final Class<?> aClass) {
        String name = aClass.getSimpleName() + ".class";
        try {
            return new URL(aClass.getResource(name).toString().replace(name, EMPTY));
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
