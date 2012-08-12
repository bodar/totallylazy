package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Files.emptyTemporaryDirectory;
import static com.googlecode.totallylazy.Files.file;
import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZipTest {
    @Test
    public void canZipAndUnzip() throws Exception {
        File playground = emptyTemporaryDirectory("totallylazy");
        File a = file(playground, "a.txt");
        File b = file(playground, "folder/b.txt");
        File zipFile = temporaryFile();

        Zip.zip(playground, zipFile);

        Files.deleteFiles(playground);
        assertThat(a.exists(), is(false));
        assertThat(b.exists(), is(false));

        Zip.unzip(zipFile, playground);

        assertThat(a.exists(), is(true));
        assertThat(b.exists(), is(true));

        Files.deleteFiles(playground);
    }
}
