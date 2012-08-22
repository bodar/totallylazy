package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public interface Destination extends Closeable {
    OutputStream destination(String name) throws IOException;
}
