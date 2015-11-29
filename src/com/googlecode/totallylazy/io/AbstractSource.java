package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.functions.Function0;
import com.googlecode.totallylazy.functions.Lazy;

import java.io.InputStream;
import java.util.Date;

public class AbstractSource extends Eq implements Source {
    private final String name;
    private final Lazy<Date> modified;
    private final Lazy<InputStream> input;
    private final boolean isDirectory;

    protected AbstractSource(String name, Function0<Date> modified, Function0<InputStream> input, boolean isDirectory) {
        this.name = name;
        this.modified = Lazy.lazy(modified);
        this.input = Lazy.lazy(input);
        this.isDirectory = isDirectory;
    }

    @multimethod
    public boolean equals(AbstractSource source) {
        return name.equals(source.name) && isDirectory == source.isDirectory;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * Boolean.valueOf(isDirectory).hashCode() * 31;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Date modified() {
        return modified.value();
    }

    @Override
    public InputStream input() {
        return input.value();
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }
}
