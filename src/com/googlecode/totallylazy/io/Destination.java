package com.googlecode.totallylazy.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public interface Destination extends Closeable {
    OutputStream destination(String name, Date modified) throws IOException;
}
