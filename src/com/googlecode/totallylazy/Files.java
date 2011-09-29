package com.googlecode.totallylazy;

import java.io.*;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.doThen;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
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

    public static Callable1<File, String> name() {
        return new Callable1<File, String>() {
            public String call(File file) throws Exception {
                return file.getName();
            }
        };
    }

    public static Callable1<? super File, String> path() {
        return new Callable1<File, String>() {
            public String call(File file) throws Exception {
                return file.getPath();
            }
        };
    }

    public static Callable1<? super File, File> parent() {
        return new Callable1<File, File>() {
            public File call(File file) throws Exception {
                return file.getParentFile();
            }
        };
    }

    public static File temporaryDirectory() {
        return temporaryDirectory(randomFilename());
    }

    public static File temporaryDirectory(String name) {
        File directory = new File(TEMP_DIR, name);
        recursiveFiles(directory).map(delete()).realise();
        directory.mkdirs();
        return directory;
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

    public static Sequence<File> recursiveFiles(final File directory){
        return files(directory).flatMap(recursiveFiles());
    }

    public static Callable1<File, Iterable<File>> recursiveFiles() {
        return new Callable1<File, Iterable<File>>() {
            public Iterable<File> call(File file) throws Exception {
                return file.isDirectory() ? sequence(file).join(recursiveFiles(file)) : sequence(file);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static File write(byte[] bytes, File file) {
        try {
            return using(new FileOutputStream(file), doThen(Runnables.write(bytes), returns(file)));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
        }
    }

    public static Callable1<? super File, Boolean> delete() {
        return new Callable1<File, Boolean>() {
            public Boolean call(File file) throws Exception {
                return file.delete();
            }
        };
    }

    public static Callable1<? super String,File> asFile() {
        return new Callable1<String, File>() {
            public File call(String name) throws Exception {
                return new File(name);
            }
        };
    }
}
