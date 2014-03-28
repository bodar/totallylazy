package com.googlecode.totallylazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Streams.emptyInputStream;

public class FileSource implements Sources {
    private final CloseableList closeables;
    private final Sequence<Source> sources;

    private FileSource(final Sequence<Pair<String, File>> sources) {
        closeables = new CloseableList();
        this.sources = sources.map(new Function<Pair<String, File>, Source>() {
            @Override
            public Source call(Pair<String, File> pair) throws Exception {
                return new Source(pair.first(), new Date(pair.second().lastModified()), inputStream(pair.second()), pair.second().isDirectory());
            }
        });
    }

    private InputStream inputStream(File file) throws FileNotFoundException {
        if (file.isDirectory()) return emptyInputStream();
        return closeables.manage(new FileInputStream(file));
    }

    public static FileSource fileSource(File folder) {
        return fileSource(folder, Files.recursiveFilesDirectoriesFirst(folder));
    }

    public static FileSource fileSource(File folder, Sequence<File> files) {
        return fileSource(files.map(relativeTo(folder)));
    }

    public static FileSource fileSource(final Iterable<? extends Pair<String, File>> sources) {
        return new FileSource(sequence(sources));
    }


    @Override
    public Sequence<Source> sources() {
        return sources;
    }

    @Override
    public void close() throws IOException {
        closeables.close();
    }

    public static Function<File, Pair<String, File>> relativeTo(final File folder) {
        return new Function<File, Pair<String, File>>() {
            @Override
            public Pair<String, File> call(File file) throws Exception {
                return Pair.pair(Files.relativePath(folder, file), file);
            }
        };
    }
}
