package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;
import java.util.Date;

import static com.googlecode.totallylazy.Files.emptyVMDirectory;
import static com.googlecode.totallylazy.Files.file;
import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.time.Dates.date;

public class ZipTest {
    @Test
    public void canZipAndUnzip() throws Exception {
        File playground = emptyVMDirectory("totallylazy");
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
        zipFile.delete();
    }

    @Test
    public void preservesModifiedDate() throws Exception {
        File playground = emptyVMDirectory("totallylazy");
        File a = file(playground, "a.txt");
        Date date = date(2001, 1, 10);
        a.setLastModified(date.getTime());
        File zipFile = temporaryFile();

        Zip.zip(playground, zipFile);
        Files.deleteFiles(playground);
        Zip.unzip(zipFile, playground);

        assertThat(a.exists(), is(true));
        assertThat(new Date(a.lastModified()), is(date));

        Files.deleteFiles(playground);
        zipFile.delete();
    }
}
