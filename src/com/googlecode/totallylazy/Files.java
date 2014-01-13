package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.AbstractPredicate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.doThen;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.time.Dates.date;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

public class Files {
    public static final File TEMP_DIR = new File(getProperty("java.io.tmpdir"));

    public static String relativePath(File folder, File file) {
        return folder.toURI().relativize(file.toURI()).getPath();
    }

    public static Predicate<File> isFile() {
        return File::isFile;
    }

    public static Predicate<File> hasSuffix(final String suffix) {
        return file -> file.getName().endsWith("." + suffix);
    }

    public static Predicate<File> isDirectory() {
        return File::isDirectory;
    }

    public static Predicate<File> hasName(final String name) {
        return where(name(), is(name));
    }


    public static Function<File, String> name() {
        return File::getName;
    }

    public static Function<File, Sequence<File>> files() {
        return Files::files;
    }

    public static Function<File, String> path() {
        return File::getPath;
    }

    public static Function<File, File> parent() {
        return File::getParentFile;
    }

    public static File temporaryDirectory() {
        return TEMP_DIR;
    }

    public static File temporaryDirectory(String name) {
        return directory(TEMP_DIR, name);
    }


    public static File emptyVMDirectory(String name) {
        return emptyTemporaryDirectory(format("%s-%s", name, getProperty("java.version")));
    }

    public static File emptyTemporaryDirectory(String name) {
        File directory = directory(TEMP_DIR, name);
        deleteFiles(directory);
        return directory;
    }

    public static boolean delete(File file) {
        return deleteFiles(file) && file.delete();
    }

    public static boolean deleteFiles(File file) {
        return recursiveFiles(file).map(deleteFile()).forAll(is(true));
    }

    public static Function<File, Boolean> deleteFile() {
        return File::delete;
    }

    public static Function<File, Boolean> delete() {
        return Files::delete;
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
            throw LazyException.lazyException(e);
        }
    }

    public static File workingDirectory() {
        return new File(getProperty("user.dir"));
    }

    public static String randomFilename() {
        return randomUUID().toString();
    }

    public static Sequence<File> files(File directory) {
        return sequence(directory.listFiles());
    }

    public static Predicate<File> containsFile(final String fileName) {
        return containsFile(hasName(fileName));
    }

    public static Predicate<File> containsFile(final Predicate<? super File> predicate) {
        return where(Files.files(), Predicates.<File>exists(predicate));
    }

    public static Sequence<File> ancestorsAndSelf(File file) {
        if (file.getParentFile() == null) return sequence(file);
        return sequence(file).join(ancestorsAndSelf(file.getParentFile()));
    }

    public static Sequence<File> ancestors(File file) {
        if (file.getParentFile() == null) return Sequences.empty();
        return sequence(file.getParentFile()).join(ancestors(file.getParentFile()));
    }

    public static Sequence<File> recursiveFiles(final File directory) {
        return files(directory).flatMap(recursiveFiles());
    }

    public static Function<File, Iterable<File>> recursiveFiles() {
        return (file) -> file.isDirectory() ? recursiveFiles(file).append(file) : sequence(file);
    }

    public static Sequence<File> recursiveFilesDirectoriesFirst(final File directory) {
        return files(directory).flatMap(recursiveFilesDirectoriesFirst());
    }

    public static Function<File, Iterable<File>> recursiveFilesDirectoriesFirst() {
        return (file) -> file.isDirectory() ? recursiveFilesDirectoriesFirst(file).cons(file) : sequence(file);
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
            throw LazyException.lazyException(e);
        }
    }

    public static Block<InputStream> write(final File output) {
        return new Block<InputStream>() {
            @Override
            protected void execute(InputStream inputStream) throws Exception {
                Streams.copyAndClose(inputStream, new FileOutputStream(output));
            }
        };
    }

    public static Function<String, File> asFile() {
        return File::new;
    }

    public static File directory(File parent, String name) {
        File child = new File(parent, name);
        child.mkdirs();
        return child;
    }

    public static File file(File parent, String name) {
        File child = new File(parent, name);
        if (child.isDirectory()) {
            throw new IllegalArgumentException(format("%s is a isDirectory", child));
        }
        try {
            child.getParentFile().mkdirs();
            child.createNewFile();
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
        return child;
    }

    public static Option<File> fileOption(File parent, String name) {
        final File file = new File(parent, name);
        return file.exists() ? some(file) : none(File.class);
    }

    public static Function<File, Date> lastModified() {
        return file -> date(file.lastModified());
    }
}