package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.functions.Function0;

import java.io.InputStream;
import java.util.Date;

public interface Source {
    static Source source(String name, Function0<Date> modified, Function0<InputStream> input, boolean isDirectory) {
        return new AbstractSource(name, modified, input, isDirectory);
    }

    String name();

    Date modified();

    InputStream input();

    boolean isDirectory();
}
