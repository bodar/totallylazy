package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

/** @deprecated Replaced by {@link com.googlecode.totallylazy.collections.CloseableList}  } */
@Deprecated
public class CloseableList extends ArrayList<Closeable> implements Closeable {
    private static final long serialVersionUID = 6663452581122892189L;

    public void close() throws IOException {
        sequence(this).each(Closeables.safeClose());
        clear();
    }

    public <T extends Closeable> T manage(T instance) {
        add(instance);
        return instance;
    }
}
