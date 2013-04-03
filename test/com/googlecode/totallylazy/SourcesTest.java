package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.googlecode.totallylazy.Files.emptyVMDirectory;
import static com.googlecode.totallylazy.Files.file;
import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Sources.constructors.sources;
import static com.googlecode.totallylazy.Uri.packageUri;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.totallylazy.Zip.zip;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        assertThat(sources.sources().size(), is(3));
        sources.close();
    }

    @Test
    public void canReadSourcesFromZipUri() throws IOException {
        File zipFile = temporaryFile();
        zip(files(), zipFile);

        Sources sources = sources(uri(String.format("jar:file:%s!/folder/", zipFile)));
        assertThat(sources.sources().size(), is(2));
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
