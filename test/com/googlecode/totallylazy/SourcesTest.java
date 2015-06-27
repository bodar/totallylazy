package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.googlecode.totallylazy.Files.emptyVMDirectory;
import static com.googlecode.totallylazy.Files.file;
import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Sources.constructors.sources;
import static com.googlecode.totallylazy.Sources.functions.directory;
import static com.googlecode.totallylazy.Sources.functions.name;
import static com.googlecode.totallylazy.http.Uri.packageUri;
import static com.googlecode.totallylazy.http.Uri.uri;
import static com.googlecode.totallylazy.Zip.zip;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.predicates.Not.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;

public class SourcesTest {
    @Test
    public void canReadSourcesFromPackageUri() throws IOException {
        Sources sources = sources(packageUri(SourcesTest.class));
        assertThat(sources.sources().size(), greaterThan(1));
        sources.close();
    }

    @Test
    public void canReadSourcesFromFileUri() throws IOException {
        File files = files();
        Sources sources = sources(uri(files));
        assertThat(sources.sources().size(), is(4));
        sources.close();
    }

    @Test
    public void canReadSourcesFromZipUri() throws IOException {
        File zipFile = temporaryFile();
        zip(files(), zipFile);

        Sources sources = sources(uri(String.format("jar:file:%s!/", zipFile)));
        assertThat(sources.sources().size(), is(4));
        sources.close();
    }

    @Test
    public void canReadSourcesFromZipUriSubFolder() throws IOException {
        File zipFile = temporaryFile();
        zip(files(), zipFile);

        Sources sources = sources(uri(String.format("jar:file:%s!/folder/", zipFile)));
        List<String> names = sources.sources().map(Sources.functions.name).toList();
        assertThat(names, containsInAnyOrder("b.txt", "c.txt"));
        sources.close();
    }

    @Test
    public void canFilterDirectories() throws IOException {
        File zipFile = temporaryFile();
        zip(files(), zipFile);

        Sources sources = sources(uri(String.format("jar:file:%s!/", zipFile)));
        List<String> names = sources.sources().filter(not(directory)).map(name).toList();
        assertThat(names, containsInAnyOrder("a.txt", "folder/b.txt", "folder/c.txt"));
        sources.close();
    }

    private File files() {
        File directory = emptyVMDirectory("totallylazy");
        file(directory, "a.txt");
        file(directory, "folder/b.txt");
        file(directory, "folder/c.txt");
        return directory;
    }
}
