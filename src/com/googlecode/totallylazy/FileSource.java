package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSource implements Source {
    private final CloseableList closeables;
    private final Sequence<Pair<String, InputStream>> sources;

    private FileSource(final Sequence<Pair<String, File>> sources) {
        closeables = new CloseableList();
        this.sources = sources.map(Callables.<String, File, InputStream>second(new Function1<File, InputStream>() {
            @Override
            public InputStream call(File file) throws Exception {
                return closeables.manage(new FileInputStream(file));
            }
        }));
    }

    public static FileSource fileSource(File folder) {
        return fileSource(folder, Files.recursiveFiles(folder));
    }

    public static FileSource fileSource(File folder, Sequence<File> files) {
        return new FileSource(files.filter(Files.isFile()).map(relativeTo(folder)));
    }

    @Override
    public Sequence<Pair<String, InputStream>> sources() {
        return sources;
    }

    @Override
    public void close() throws IOException {
        closeables.close();
    }

    public static Function1<File, Pair<String, File>> relativeTo(final File folder) {
        return new Function1<File, Pair<String, File>>() {
            @Override
            public Pair<String, File> call(File file) throws Exception {
                return Pair.pair(Files.relativePath(folder, file), file);
            }
        };
    }
}
