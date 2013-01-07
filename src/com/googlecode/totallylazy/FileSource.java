package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class FileSource implements Sources {
    private final CloseableList closeables;
    private final Sequence<Source> sources;

    private FileSource(final Sequence<Pair<String, File>> sources) {
        closeables = new CloseableList();
        this.sources = sources.map(new Function1<Pair<String, File>, Source>() {
            @Override
            public Source call(Pair<String, File> pair) throws Exception {
                return new Source(pair.first(), new Date(pair.second().lastModified()), closeables.manage(new FileInputStream(pair.second())));
            }
        });
    }

    public static FileSource fileSource(File folder) {
        return fileSource(folder, Files.recursiveFiles(folder));
    }

    public static FileSource fileSource(File folder, Sequence<File> files) {
        return new FileSource(files.filter(Files.isFile()).map(relativeTo(folder)));
    }

    @Override
    public Sequence<Source> sources() {
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
