package com.googlecode.totallylazy;

import java.io.*;

import static com.googlecode.totallylazy.Bytes.bytes;
import static com.googlecode.totallylazy.Callables.doThen;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

public class Files {
    public static final File TEMP_DIR = new File(getProperty("java.io.tmpdir"));

    public static Predicate<? super File> isFile() {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.isFile();
            }
        };
    }

    public static Predicate<? super File> isDirectory() {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.isDirectory();
            }
        };
    }

    public static Function1<File, String> name() {
        return new Function1<File, String>() {
            public String call(File file) throws Exception {
                return file.getName();
            }
        };
    }

    public static Function1<File, String> path() {
        return new Function1<File, String>() {
            public String call(File file) throws Exception {
                return file.getPath();
            }
        };
    }

    public static Function1<File, File> parent() {
        return new Function1<File, File>() {
            public File call(File file) throws Exception {
                return file.getParentFile();
            }
        };
    }

    public static File temporaryDirectory() {
        return temporaryDirectory(randomFilename());
    }

    public static File temporaryDirectory(String name) {
        File directory = directory(TEMP_DIR, name);
        delete(directory);
        return directory;
    }

    public static boolean delete(File file) {
        return recursiveFiles(file).map(delete()).forAll(is(true));
    }

    public static File temporaryFile() {
        return temporaryFile(TEMP_DIR);
    }

    public static File temporaryFile(File directory) {
        try {
            File file = new File(directory, randomFilename());
            file.deleteOnExit();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public static File workingDirectory() {
        return new File(".");
    }

    public static String randomFilename() {
        return randomUUID().toString();
    }

    public static Sequence<File> files(File directory) {
        return sequence(directory.listFiles());
    }

    public static Sequence<File> recursiveFiles(final File directory) {
        return files(directory).flatMap(recursiveFiles());
    }

    public static Function1<File, Iterable<File>> recursiveFiles() {
        return new Function1<File, Iterable<File>>() {
            public Iterable<File> call(File file) throws Exception {
                return file.isDirectory() ? recursiveFiles(file).add(file) : sequence(file);
            }
        };
    }

    public static File write(byte[] bytes, File file) {
        return write(bytes, file, false);
    }

    public static File append(byte[] bytes, File file) {
        return write(bytes, file, true);
    }

    public static File write(byte[] bytes, File file, boolean append) {
        try {
            return using(new FileOutputStream(file, append), doThen(Runnables.write(bytes), returns(file)));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
        }
    }

    public static Function1<InputStream, Void> write(final File output) {
        return new Function1<InputStream, Void>() {
            @Override
            public Void call(InputStream inputStream) throws Exception {
                write(bytes(inputStream), output);
                return null;
            }
        };
    }

    public static Function1<File, Boolean> delete() {
        return new Function1<File, Boolean>() {
            public Boolean call(File file) throws Exception {
                return file.delete();
            }
        };
    }

    public static Function1<String, File> asFile() {
        return new Function1<String, File>() {
            public File call(String name) throws Exception {
                return new File(name);
            }
        };
    }

    public static File directory(File parent, String name) {
        File child = new File(parent, name);
        child.mkdirs();
        return child;
    }

    public static File file(File parent, String name) {
        File child = new File(parent, name);
        if (child.isDirectory()) {
            throw new IllegalArgumentException(format("%s is a directory", child));
        }
        try {
            child.createNewFile();
        } catch (IOException e) {
            throw new LazyException(e);
        }
        return child;
    }
}
