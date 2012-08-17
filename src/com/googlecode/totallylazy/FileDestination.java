package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    public OutputStream destination(String name) throws IOException {
        File file = new File(folder, name);
        file.getParentFile().mkdirs();
        return closeables.manage(new FileOutputStream(file));
    }

    @Override
    public void close() throws IOException {
        closeables.close();
    }
}
