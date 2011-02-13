package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Files.*;
import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class FilesTest {
    @Test
    public void supportsFiltering() throws Exception {
        File directory = temporaryDirectory();
        File aFile = temporaryFile(directory);
        File anOtherFile = temporaryFile(directory);
        Sequence<File> files = files(directory);
        assertThat(files, containsInAnyOrder(aFile, anOtherFile));
        assertThat(files.filter(where(name(), is(equalTo(aFile.getName())))), hasExactly(aFile));
    }

}
