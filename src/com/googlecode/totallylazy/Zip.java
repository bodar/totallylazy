package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ZipEntryIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.googlecode.totallylazy.FileDestination.fileDestination;
import static com.googlecode.totallylazy.FileSource.fileSource;
import static com.googlecode.totallylazy.Sources.methods.copyAndClose;
import static com.googlecode.totallylazy.ZipDestination.zipDestination;
import static com.googlecode.totallylazy.ZipSource.zipSource;

public class Zip {
    public static Number zip(final File folder, File zipFile) throws IOException {
        return zip(folder, new FileOutputStream(zipFile));
    }

    public static Number zip(final File folder, OutputStream outputStream) throws IOException {
        return copyAndClose(fileSource(folder), zipDestination(outputStream));
    }

    public static Number unzip(final File zipFile, final File folder) throws IOException {
        return unzip(new FileInputStream(zipFile), folder);
    }

    public static Number unzip(InputStream in, final File folder) {
        return copyAndClose(zipSource(in), fileDestination(folder));
    }

    public static Sequence<ZipEntry> entries(final ZipInputStream input) {
        return new Sequence<ZipEntry>() {
            @Override
            public Iterator<ZipEntry> iterator() {
                return new ZipEntryIterator(input);
            }
        };
    }

}
