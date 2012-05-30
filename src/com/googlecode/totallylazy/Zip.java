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
import java.util.zip.ZipOutputStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Files.recursiveFiles;
import static com.googlecode.totallylazy.Runnables.VOID;

public class Zip {
    public static Number zip(final File folder, File zipFile) throws IOException {
        return zip(folder, new FileOutputStream(zipFile));
    }

    public static Number zip(final File folder, OutputStream outputStream) throws IOException {
        final Sequence<File> files = recursiveFiles(folder);
        return using(new ZipOutputStream(outputStream), new Callable1<ZipOutputStream, Number>() {
            @Override
            public Number call(final ZipOutputStream out) throws Exception {
                return files.map(new Callable1<File, Void>() {
                    @Override
                    public Void call(final File file) throws Exception {
                        return using(new FileInputStream(file), new Callable1<InputStream, Void>() {
                            @Override
                            public Void call(InputStream input) throws Exception {
                                addEntry(Files.relativePath(folder, file), input, out);
                                return VOID;
                            }
                        });
                    }
                }).size();
            }
        });

    }

    public static Number unzip(final File zipFile, final File folder) throws IOException {
        return unzip(folder, new FileInputStream(zipFile));
    }

    public static Number unzip(final File folder, InputStream in) {
        return using(new ZipInputStream(in), new Callable1<ZipInputStream, Number>() {
            @Override
            public Number call(final ZipInputStream input) throws Exception {
                return entries(input).map(new Callable1<ZipEntry, Void>() {
                    @Override
                    public Void call(final ZipEntry entry) throws Exception {
                        return using(new FileOutputStream(new File(folder, entry.getName())), new Callable1<OutputStream, Void>() {
                            @Override
                            public Void call(OutputStream output) throws Exception {
                                Streams.copy(input, output);
                                return VOID;
                            }
                        });
                    }
                }).size();
            }
        });
    }

    public static Sequence<ZipEntry> entries(final ZipInputStream input) {
        return new Sequence<ZipEntry>() {
            @Override
            public Iterator<ZipEntry> iterator() {
                return new ZipEntryIterator(input);
            }
        };
    }


    public static void addEntry(String entryName, InputStream input, ZipOutputStream out) throws IOException {
        out.putNextEntry(new ZipEntry(entryName));
        Streams.copy(input, out);
        out.closeEntry();
    }

}
