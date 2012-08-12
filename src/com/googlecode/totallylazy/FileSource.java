package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSource implements Source {
    private final CloseableList closeables;
    private final File folder;

    private FileSource(File folder) {
        this.folder = folder;
        closeables = new CloseableList();
    }

    public static FileSource fileSource(File folder) {
        return new FileSource(folder);
    }

    @Override
    public Sequence<Pair<String, InputStream>> sources() {
        return Files.recursiveFiles(folder).filter(Files.isFile()).map(relativeTo(folder));
    }

    @Override
    public void close() throws IOException {
        closeables.close();
    }

    private Function1<File, Pair<String, InputStream>> relativeTo(final File folder) {
        return new Function1<File, Pair<String, InputStream>>() {
            @Override
            public Pair<String, InputStream> call(File file) throws Exception {
                return Pair.<String, InputStream>pair(Files.relativePath(folder, file),
                        closeables.manage(new FileInputStream(file)));
            }
        };
    }
}
