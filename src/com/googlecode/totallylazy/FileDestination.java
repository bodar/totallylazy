package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Streams.nullOutputStream;

public class FileDestination implements Destination {
    private final CloseableList closeables;
    private final File folder;

    private FileDestination(File folder) {
        this.folder = folder;
        closeables = new CloseableList();
    }

    public static FileDestination fileDestination(File folder) {
        return new FileDestination(folder);
    }

    @Override
    public OutputStream destination(String name, final Date modified) throws IOException {
        final File file = new File(folder, name);
        file.getParentFile().mkdirs();

        if (name.endsWith("/")) {
            file.mkdir();
            file.setLastModified(modified.getTime());
            return nullOutputStream();
        }
        return closeables.manage(new FileOutputStream(file) {
            @Override
            public void close() throws IOException {
                super.close();
                file.setLastModified(modified.getTime());
            }
        });
    }

    @Override
    public void close() throws IOException {
        closeables.close();
    }
}
