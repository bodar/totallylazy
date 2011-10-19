package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class CloseableList extends ArrayList<Closeable> implements Closeable{
    public void close() throws IOException {
        sequence(this).each(Closeables.close());
        clear();
    }
}
